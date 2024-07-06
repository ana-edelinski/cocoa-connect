package dto;

import java.util.Date;

import javax.json.bind.annotation.JsonbDateFormat;

import beans.User;
import enums.Gender;
import enums.Role;
import enums.UserType;

public class UserDto {
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
	private int points;
	private int factoryWorkingId;
	private UserType type;

	public UserDto() {
		super();
	}

	public UserDto(User user) {
		id = user.getId();
		username = user.getUsername();
		password = user.getPassword();
		name = user.getName();
		surname = user.getSurname();
		gender = user.getGender();
		dateOfBirth = user.getDateOfBirth();
		role = user.getRole();
		isAssigned = user.getIsAssigned();
		points = user.getPoints();
		factoryWorkingId = user.getFactoryWorkingId();
		type = user.getType();
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

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
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

	public boolean isAssigned() {
		return isAssigned;
	}

	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

	public int getFactoryWorkingId() {
		return factoryWorkingId;
	}

	public void setFactoryWorkingId(int factoryWorkingId) {
		this.factoryWorkingId = factoryWorkingId;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}
	

}
