package dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import beans.Chocolate;
import beans.User;
import enums.ChocolateKind;
import enums.ChocolateStatus;
import enums.ChocolateType;

public class ChocolateDto {
	private int id;
 	private String name;
    private double price;
    private ChocolateKind kind;
	private ChocolateType type;
    private int weight;
    private String description;
    private int quantity;
    

    public ChocolateDto() {
		super();
	}

    public Chocolate convertToChocolate() {
		Chocolate chocolate = new Chocolate();
		chocolate.setName(name);
		chocolate.setPrice(price);
		chocolate.setKind(kind);
		chocolate.setType(type);
		chocolate.setWeight(weight);
		chocolate.setDescription(description);
		chocolate.setQuantity(quantity);
		
		return chocolate;
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


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
    
}