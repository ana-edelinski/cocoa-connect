package beans;

import enums.ChocolateKind;
import enums.ChocolateStatus;
import enums.ChocolateType;

public class Chocolate {
	private int id;
	private String name;
	private double price;
	private ChocolateKind kind;
	private Integer factory;
	private ChocolateType type;
	private int weight;
	private String description;
	private String image;
	private ChocolateStatus status;
	private int quantity;
	private boolean deleted;

	public Chocolate() {
	}

	public Chocolate(int id, String name, double price, String kind, Integer factory, String type, int weight,
			String description, String image, int quantity, boolean deleted) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.kind = ChocolateKind.valueOf(kind.toUpperCase());
		this.factory = factory;
		this.type = ChocolateType.valueOf(type.toUpperCase());
		this.weight = weight;
		this.description = description;
		this.image = image;
		this.status = ChocolateStatus.OUT_OF_STOCK;
		this.quantity = quantity;
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Integer getFactory() {
		return factory;
	}

	public void setFactory(Integer factory) {
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
