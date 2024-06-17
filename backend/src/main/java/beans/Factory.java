package beans;

import enums.FactoryStatus;

public class Factory {
	private int id;
	private String name;
	private FactoryStatus factoryStatus;
	private String city;
	private String country;
	private double averageRating;
	private boolean deleted;
	private String logo; 
	private WorkingHours workingHours;
	private String comment; 
	private int managerId;
	
	public Factory() {}

	public Factory(int id, String name, String factoryStatus, String city, String country,
			double averageRating, boolean deleted, String logo, WorkingHours workingHours, String comment, int managerId) {
		super();
		this.id = id;
		this.name = name;
		this.factoryStatus = FactoryStatus.valueOf(factoryStatus.toUpperCase());
		this.city = city;
		this.country = country;
		this.averageRating = averageRating;
		this.deleted = deleted;
		this.logo = logo;
		this.workingHours = workingHours;
		this.comment = comment; 
		this.managerId = managerId;
	}

	// Getter and setter methods for comment
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
	
}
