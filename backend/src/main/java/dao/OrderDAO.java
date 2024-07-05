package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.criteria.CriteriaBuilder.In;

import beans.Chocolate;
import beans.Factory;
import beans.Order;
import beans.OrderItem;
import beans.User;
import dto.CartDto;
import dto.CartItemDto;
import dto.ChocolateDto;
import enums.OrderStatus;

public class OrderDAO {
	private HashMap<Integer, Order> orders = new HashMap<>();
	private String contextPath;

	public OrderDAO() {
	}

	public OrderDAO(String contextPath) {
		this.contextPath = contextPath;
		loadOrders(contextPath);
	}

	public Collection<Order> findAll() {
		Collection<Order> result = new ArrayList<>();
		for (var c : orders.values()) {

			result.add(c);
		}
		return result;
	}

	public Order findById(int id) {
		return orders.containsKey(id) ? orders.get(id) : null;
	}

	public Collection<Order> findAllByUser(Integer userId) {
		List<Order> result = new ArrayList<>();
		for (Order order : findAll()) {
			if (order.getUser().getId() == userId) {
				result.add(order);
			}
		}
		return result;
	}

	public Collection<Order> findAllByFactory(Integer factoryId) {
		List<Order> result = new ArrayList<>();
		for (Order order : findAll()) {
			if (order.getFactory().getId() == factoryId) {
				result.add(order);
			}
		}
		return result;
	}
//	public Order update(int id, Order order) {
//		Order o = orders.containsKey(id) ? orders.get(id) : null;
//		if (o == null) {
//			return save(order);
//		} else {
//			o.setUser(order.getUser());
//			o.setPrice(order.getPrice());
//		}
//		saveToFile(contextPath);
//		return o;
//	}

	public int generateNextId() {
		Integer maxId = -1;
		for (Integer id : orders.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		return maxId;
	}
	
	public Order cancel(int id) {
		Order order = findById(id);
		order.setStatus(OrderStatus.CANCELED);
		saveToFile(contextPath);
		
		User user = order.getUser();
		double lostPoints = order.getPrice() / 1000 * 133 * 4;
		user.setPoints(user.getPoints() - (int)lostPoints);
		
		UserDAO userDAO = new UserDAO(contextPath);
		userDAO.update(user.getId(), user);
		return order;
	}
	public Order aprove(int id) {
		Order order = findById(id);
		order.setStatus(OrderStatus.APPROVED);
		saveToFile(contextPath);
		return order;
	}	
	public Order reject(int id, String comment) {
	    Order order = findById(id);
	    if (order != null) {
	        order.setStatus(OrderStatus.REJECTED);
	        order.setComment(comment);
	        saveToFile(contextPath);
	    }
	    return order;
	}
	public Order saveCart(CartDto cartDto) {
		UserDAO userDAO = new UserDAO(contextPath);
		FactoryDAO factoryDAO = new FactoryDAO(contextPath);
		ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
		OrderItemDAO orderItemDAO = new OrderItemDAO(contextPath);

		int userId = cartDto.getLoggedId();
		User user = userDAO.findById(userId);
		Factory factory = factoryDAO.findById(cartDto.getFactoryId());

		Order order = new Order();
		order.setDate(LocalDateTime.now());
		order.setId(generateNextId());
		order.setUser(user);
		order.setStatus(OrderStatus.PROCESSING);
		order.setFactory(factory);

		double ukupnaCena = 0;
		for (CartItemDto cartItemDto : cartDto.getItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setQuantity(cartItemDto.getQuantity());
			orderItem.setOrder(order);
			int chocolateId = cartItemDto.getChocolate().getId();
			Chocolate chocolate = chocolateDAO.findById(chocolateId);

			orderItem.setChocolate(chocolate);
			orderItemDAO.save(orderItem);
			ukupnaCena += cartItemDto.getQuantity() * chocolate.getPrice();
		}

		order.setPrice(ukupnaCena);
		orders.put(order.getId(), order);
		saveToFile(contextPath);
		
		double points = order.getPrice() / 1000 * 133;
		user.setPoints(user.getPoints() + (int)points);
		
		userDAO.update(user.getId(), user);
		return order;
	}

	private void loadOrders(String contextPath) {
		BufferedReader in = null;

		UserDAO userDAO = new UserDAO(contextPath);
		FactoryDAO factoryDAO = new FactoryDAO(contextPath);
		try {
			File file = new File(contextPath + "/orders.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", userId = "", price = "", status = "", date = "", factoryId = "", comment = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					userId = st.nextToken().trim();
					price = st.nextToken().trim();
					status = st.nextToken().trim();
					date = st.nextToken().trim();
					factoryId = st.nextToken().trim();
					comment = st.hasMoreTokens() ? st.nextToken().trim() : "";
				}

				int idO = Integer.parseInt(id);
				int userIdInt = Integer.parseInt(userId);
				int factoryIdInt = Integer.parseInt(factoryId);
				User user = userDAO.findById(userIdInt);
				Factory factory = factoryDAO.findById(factoryIdInt);
				double pricee = Double.parseDouble(price);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateNow = LocalDateTime.parse(date, formatter);
				orders.put(Integer.parseInt(id),
						new Order(idO, user, pricee, OrderStatus.valueOf(status.toUpperCase()), dateNow, factory, comment));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean saveToFile(String path) {
		BufferedWriter out = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		try {
			File file = new File(path + "/orders.csv");
			out = new BufferedWriter(new FileWriter(file));

			for (Order order : orders.values()) {
				StringBuilder line = new StringBuilder();
				line.append(order.getId()).append(";");
				line.append(order.getUser().getId()).append(";");
				line.append(order.getPrice()).append(";");
				line.append(order.getStatus()).append(";");
				line.append(order.getDate().format(formatter)).append(";");
				line.append(order.getFactory().getId()).append(";");
				line.append(order.getComment() != null ? order.getComment() : "");
				out.write(line.toString());
				out.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	
	

	public Order deleteOrder(int id) {
		Order order = findById(id);
		if (order == null)
			return null;
		// order.setDeleted(true);
		return saveToFile(contextPath) ? order : null;
	}
	
	
	
	 
}
