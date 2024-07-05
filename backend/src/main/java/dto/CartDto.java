package dto;

import java.util.ArrayList;

public class CartDto {
	private ArrayList<CartItemDto> items;
	private int loggedId;
	private int factoryId;

	public CartDto() {
		super();
	}

	public ArrayList<CartItemDto> getItems() {
		return items;
	}

	public void setItems(ArrayList<CartItemDto> items) {
		this.items = items;
	}

	public int getLoggedId() {
		return loggedId;
	}

	public void setLoggedId(int loggedId) {
		this.loggedId = loggedId;
	}

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

}