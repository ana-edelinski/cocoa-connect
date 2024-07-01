package beans;

import enums.OrderStatus;

public class Order {
	private int id;
	private User user;
	private double price;
	private OrderStatus status;
	
	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Order() {
		super();
	}
	
	public Order(int id, User user, double price, OrderStatus status) {
		super();
		this.id = id;
		this.user = user;
		this.price = price;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
