package beans;

import java.time.LocalDateTime;

import enums.OrderStatus;

public class Order {
	private int id;
	private User user;
	private double price;
	private OrderStatus status;
	private LocalDateTime date;
	private Factory factory;
	private String comment;
	
	public Order() {
		super();
	}

	public Order(int id, User user, double price, OrderStatus status, LocalDateTime date, Factory factory, String comment) {
		super();
		this.id = id;
		this.user = user;
		this.price = price;
		this.status = status;
		this.date = date;
		this.factory = factory;
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Factory getFactory() {
		return factory;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}
	

}