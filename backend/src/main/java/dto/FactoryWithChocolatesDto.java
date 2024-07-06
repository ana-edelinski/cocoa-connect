package dto;

import java.util.ArrayList;

import beans.Chocolate;
import beans.Factory;
import beans.WorkingHours;
import enums.FactoryStatus;

public class FactoryWithChocolatesDto {
	private int id;
	private String name;
	private FactoryStatus factoryStatus;
	private String city;
	private String country;
	private double averageRating;
	private boolean deleted;
	private String logo;
	private WorkingHours workingHours;
	private int managerId;
	private ArrayList<Chocolate> chocolates;

	public FactoryWithChocolatesDto() {
		super();
	}
	
	public FactoryWithChocolatesDto(Factory factory) {
		super();
		id = factory.getId();
		name = factory.getName();
		factoryStatus = factory.getFactoryStatus();
		city = factory.getCity();
		country = factory.getCountry();
		averageRating = factory.getAverageRating();
		deleted = factory.isDeleted();
		logo = factory.getLogo();
		workingHours = factory.getWorkingHours();
		managerId = factory.getManagerId();
		
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

	public FactoryStatus getFactoryStatus() {
		return factoryStatus;
	}

	public void setFactoryStatus(FactoryStatus factoryStatus) {
		this.factoryStatus = factoryStatus;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public WorkingHours getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(WorkingHours workingHours) {
		this.workingHours = workingHours;
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	public ArrayList<Chocolate> getChocolates() {
		return chocolates;
	}

	public void setChocolates(ArrayList<Chocolate> chocolates) {
		this.chocolates = chocolates;
	}

}
