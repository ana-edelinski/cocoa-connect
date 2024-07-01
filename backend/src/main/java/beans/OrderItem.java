package beans;

public class OrderItem {
	private int id;
	private Chocolate chocolate;
	private int quantity;
	private Order order;
	
	public OrderItem(int id, Chocolate chocolate, int quantity, Order order) {
		super();
		this.id = id;
		this.chocolate = chocolate;
		this.quantity = quantity;
		this.order = order;
	}
	public OrderItem() {
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Chocolate getChocolate() {
		return chocolate;
	}
	public void setChocolate(Chocolate chocolate) {
		this.chocolate = chocolate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
}
