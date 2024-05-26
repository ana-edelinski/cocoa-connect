package beans;

import enums.ChocolateKind;
import enums.ChocolateStatus;
import enums.ChocolateType;

public class Chocolate {
		private String id;
	 	private String name;
	    private double price;
	    private ChocolateKind kind;
	    private String factory;
	    private ChocolateType type;
	    private int weight;
	    private String description;
	    private String image;
	    private ChocolateStatus status;
	    private int quantity;
	
	public Chocolate() {}

	public Chocolate(String id, String name, double price, String kind, String factory, String type, int weight,
            String description, String image, String status, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.kind = ChocolateKind.valueOf(kind.toUpperCase());
		this.factory = factory;
		this.type = ChocolateType.valueOf(type.toUpperCase());
		this.weight = weight;
		this.description = description;
		this.image = image;
		this.status = ChocolateStatus.valueOf(status.toUpperCase());
		this.quantity = quantity;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ChocolateKind getKind() {
		return kind;
	}

	public void setKind(ChocolateKind kind) {
		this.kind = kind;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public ChocolateType getType() {
		return type;
	}

	public void setType(ChocolateType type) {
		this.type = type;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public ChocolateStatus getStatus() {
		return status;
	}

	public void setStatus(ChocolateStatus status) {
		this.status = status;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
