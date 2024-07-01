package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Order;
import beans.User;
import enums.ChocolateKind;
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

	public Order update(int id, Order order) {
		Order o = orders.containsKey(id) ? orders.get(id) : null;
		if (o == null) {
			return save(order);
		} else {
			o.setUser(order.getUser());
			o.setPrice(order.getPrice());
		}
		saveToFile(contextPath);
		return o;
	}

	public Order save(Order order) {
		Integer maxId = -1;
		for (Integer id : orders.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		order.setId(maxId);
		orders.put(order.getId(), order);
		saveToFile(contextPath);
		return order;
	}

	private void loadOrders(String contextPath) {
		BufferedReader in = null;

		UserDAO userDAO = new UserDAO(contextPath);
		try {
			File file = new File(contextPath + "/orders.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", userId = "", price = "", status = "";
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

				}
				
				int idO = Integer.parseInt(id);
				int userIdInt = Integer.parseInt(userId);
				User user = userDAO.findById(userIdInt);
				double pricee = Double.parseDouble(price);
				orders.put(Integer.parseInt(id), new Order(idO, user, pricee, OrderStatus.valueOf(status.toUpperCase())));
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
		try {
			File file = new File(path + "/orders.csv");
			out = new BufferedWriter(new FileWriter(file));

			for (Order order : orders.values()) {
				StringBuilder line = new StringBuilder();
				line.append(order.getId()).append(";");
				line.append(order.getUser()).append(";");
				line.append(order.getPrice()).append(";");
				line.append(order.getStatus()).append(";");
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
