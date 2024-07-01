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

import beans.Chocolate;
import beans.Factory;
import beans.Order;
import beans.OrderItem;
import beans.User;

public class OrderItemDAO {
	private HashMap<Integer, OrderItem> items = new HashMap<>();

	private String contextPath;

	public OrderItemDAO() {
	}

	public OrderItemDAO(String contextPath) {
		this.contextPath = contextPath;
		loadOrderItems(contextPath);
	}

	public Collection<OrderItem> findAll() {
		Collection<OrderItem> result = new ArrayList<>();
		for (var c : items.values()) {

			result.add(c);
		}
		return result;
	}

	public OrderItem findById(int id) {
		return items.containsKey(id) ? items.get(id) : null;
	}

	public OrderItem update(int id, OrderItem item) {
		OrderItem o = items.containsKey(id) ? items.get(id) : null;
		if (o == null) {
			return save(item);
		} else {
			o.setChocolate(item.getChocolate());
			o.setQuantity(item.getQuantity());
			o.setOrder(item.getOrder());
		}
		saveToFile(contextPath);
		return o;
	}

	public OrderItem save(OrderItem item) {
		Integer maxId = -1;
		for (Integer id : items.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		item.setId(maxId);
		items.put(item.getId(), item);
		saveToFile(contextPath);
		return item;
	}

	private void loadOrderItems(String contextPath) {
		BufferedReader in = null;

		ChocolateDAO chocolateDAO = new ChocolateDAO(contextPath);
		OrderDAO orderDAO = new OrderDAO(contextPath);

		try {
			File file = new File(contextPath + "/items.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", chocolateId = "", quantity = "", orderId = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					chocolateId = st.nextToken().trim();
					quantity = st.nextToken().trim();
					orderId = st.nextToken().trim();
				}
				int idInt = Integer.parseInt(id);
				int chocolateIdInt = Integer.parseInt(chocolateId);
				int orderIdInt = Integer.parseInt(orderId);

				Chocolate chocolate = chocolateDAO.findById(chocolateIdInt);
				Order order = orderDAO.findById(orderIdInt);

				items.put(Integer.parseInt(id), new OrderItem(idInt, chocolate, Integer.parseInt(quantity), order));
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
			File file = new File(path + "/items.csv");
			out = new BufferedWriter(new FileWriter(file));

			for (OrderItem item : items.values()) {
				StringBuilder line = new StringBuilder();
				line.append(item.getId()).append(";");
				line.append(item.getChocolate()).append(";");
				line.append(item.getQuantity()).append(";");
				line.append(item.getOrder()).append(";");
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

	public OrderItem deleteOrder(int id) {
		OrderItem item = findById(id);
		if (item == null)
			return null;
		// item.setDeleted(true);
		return saveToFile(contextPath) ? item : null;
	}

}
