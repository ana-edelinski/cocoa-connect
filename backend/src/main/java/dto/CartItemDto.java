package dto;

import java.util.ArrayList;

import beans.Chocolate;
import beans.OrderItem;

public class CartItemDto {
	private int id;
	private int quantity;
	private ArrayList<ChocolateDto> chocolates;
	public CartItemDto() {
		super();
	}
	
	public OrderItem convertToOrderItem() {
		OrderItem item = new OrderItem();
		item.setQuantity(quantity);
		return item;
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ArrayList<ChocolateDto> getChocolates() {
		return chocolates;
	}

	public void setChocolates(ArrayList<ChocolateDto> chocolates) {
		this.chocolates = chocolates;
	}
	
	
	
	
}
