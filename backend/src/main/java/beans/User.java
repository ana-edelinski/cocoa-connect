package beans;

import java.util.Date;

import javax.json.bind.annotation.JsonbDateFormat;

import enums.Gender;
import enums.Role;

public class User {
	private int id;
	private String username;
	private String password;
	private String name;
	private String surname;
	private Gender gender;
	@JsonbDateFormat("yyyy-MM-dd")
	private Date dateOfBirth;
	private Role role;
	private boolean isAssigned;
	private int factoryWorkingId;
	private int points;

	public User() {
		super();
	}

	public User(int id, String username, String password, String name, String surname, Gender gender, Date dateOfBirth,
			Role role, boolean isAssigned, int points) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.role = role;
		this.isAssigned = isAssigned;
		this.points = points;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

	public int getFactoryWorkingId() {
		return factoryWorkingId;
	}

	public void setFactoryWorkingId(int factoryWorkingId) {
		this.factoryWorkingId = factoryWorkingId;
	}

	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
