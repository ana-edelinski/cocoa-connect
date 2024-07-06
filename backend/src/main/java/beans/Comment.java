package beans;

import enums.CommentStatus;

public class Comment {
	private int id;
	private User user;
	private Factory factory;
	private Order order;
	private String text;
	private int grade;
	private CommentStatus status;

	public Comment() {
		super();
	}

	public Comment(int id, User user, Factory factory, Order order, String text, int grade, CommentStatus status) {
		super();
		this.id = id;
		this.user = user;
		this.factory = factory;
		this.order = order;
		this.text = text;
		this.grade = grade;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Factory getFactory() {
		return factory;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public CommentStatus getStatus() {
		return status;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
	}

}
