package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EnumType;

import beans.Chocolate;
import beans.Comment;
import beans.Factory;
import beans.Order;
import beans.User;
import dto.CommentCreationDto;
import enums.CommentStatus;
import enums.OrderStatus;
import enums.Role;

public class CommentDAO {
	private HashMap<Integer, Comment> comments = new HashMap<Integer, Comment>();
	public static String contextPath;

	private static CommentDAO instance;

	private CommentDAO(String contextPath) {
		loadComments(contextPath);
	}

	public static CommentDAO getInstance() {
		if (instance == null) {
			instance = new CommentDAO(contextPath);
		}
		return instance;
	}

	public Collection<Comment> findAll() {
		Collection<Comment> result = new ArrayList();

		for (var c : comments.values()) {
			result.add(c);
		}

		return result;
	}

	public Comment findById(int id) {
		return comments.containsKey(id) ? comments.get(id) : null;
	}

	public Comment getCommentForOrder(int orderId) {
		for (Comment comment : comments.values()) {
			if (comment.getOrder().getId() == orderId) {
				return comment;
			}
		}
		return null;
	}

	public Collection<Comment> getAllForFactory(int factoryId) {
		List<Comment> factoryComments = new ArrayList<>();
		for (Comment comment : comments.values()) {
			if (comment.getFactory().getId() == factoryId) {
				factoryComments.add(comment);
			}
		}
		return factoryComments;
	}

	public Collection<Comment> getAllApprovedForFactory(int factoryId) {
		List<Comment> approvedComments = new ArrayList<>();
		for (Comment comment : comments.values()) {
			if (comment.getFactory().getId() == factoryId && comment.getStatus() == CommentStatus.APPROVED) {
				approvedComments.add(comment);
			}
		}
		return approvedComments;
	}

	private int generateId() {
		Integer maxId = -1;
		for (Integer id : comments.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		return maxId;
	}

	public Comment save(CommentCreationDto commentDto) {
		OrderDAO orderDAO = OrderDAO.getInstance();
		Order order = orderDAO.findById(commentDto.getOrderId());
		if (order.getStatus() != OrderStatus.APPROVED) {
			return null;
		}

		Comment comment = new Comment();
		comment.setText(commentDto.getComment());
		comment.setGrade(commentDto.getRating());
		comment.setStatus(CommentStatus.PROCESSING);
		comment.setOrder(order);
		comment.setUser(order.getUser());
		comment.setFactory(order.getFactory());

		comment.setId(generateId());
		comments.put(comment.getId(), comment);
		saveToFile(contextPath);
		return comment;
	}

	private void loadComments(String contextPath) {
		BufferedReader in = null;

		UserDAO userDAO = UserDAO.getInstance();
		FactoryDAO factoryDAO = FactoryDAO.getInstance();
		OrderDAO orderDao = OrderDAO.getInstance();
		try {
			File file = new File(contextPath + "/comments.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", userId = "", factoryId = "", orderId = "", text = "", grade = "", status = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					userId = st.nextToken().trim();
					factoryId = st.nextToken().trim();
					orderId = st.nextToken().trim();
					text = st.nextToken().trim();
					grade = st.nextToken().trim();
					status = st.nextToken().trim();
				}

				int idC = Integer.parseInt(id);
				int userIdInt = Integer.parseInt(userId);
				int factoryIdInt = Integer.parseInt(factoryId);
				int orderIdInt = Integer.parseInt(orderId);
				User user = userDAO.findById(userIdInt);
				Factory factory = factoryDAO.findById(factoryIdInt);
				Order order = orderDao.findById(orderIdInt);
				int gradeI = Integer.parseInt(grade);
				CommentStatus statusP = CommentStatus.valueOf(status);
				comments.put(Integer.parseInt(id), new Comment(idC, user, factory, order, text, gradeI, statusP));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public boolean deleteComment(int id) {
		Comment c = findById(id);
		if (c == null)
			return false;

		return saveToFile(contextPath);
	}

	public Comment aprove(int id) {
		Comment comment = findById(id);
		comment.setStatus(CommentStatus.APPROVED);
		saveToFile(contextPath);
		return comment;
	}	
	public Comment reject(int id) {
		Comment comment = findById(id);
	    if (comment != null) {
	    	comment.setStatus(CommentStatus.REJECTED);
	        saveToFile(contextPath);
	    }
	    return comment;
	}
	
	private boolean saveToFile(String path) {
		BufferedWriter out = null;
		try {
			File file = new File(path + "comments.csv");
			out = new BufferedWriter(new FileWriter(file));

			for (Comment comment : comments.values()) {
				StringBuilder line = new StringBuilder();
				line.append(comment.getId()).append(";");
				line.append(comment.getUser().getId()).append(";");
				line.append(comment.getFactory().getId()).append(";");
				line.append(comment.getOrder().getId()).append(";");
				line.append(comment.getText()).append(";");
				line.append(comment.getGrade()).append(";");
				line.append(comment.getStatus());

				out.write(line.toString());
				out.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
}
