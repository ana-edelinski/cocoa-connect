package dto;

public class CartItemDto {
	private int quantity;
	private ChocolateDto chocolate;

	public CartItemDto() {
		super();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ChocolateDto getChocolate() {
		return chocolate;
	}

	public void setChocolate(ChocolateDto chocolate) {
		this.chocolate = chocolate;
	}

}
