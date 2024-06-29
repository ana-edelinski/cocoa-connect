package dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import beans.User;
import enums.Gender;

public class EmployeeCreationDto {
	private String username;
	private String password;
	private String name;
	private String surname;
	private Gender gender;
	private String dateOfBirth;
	private int managerId;

	public EmployeeCreationDto() {
		super();
	}
	public User convertToUser() {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setName(name);
		user.setSurname(surname);
		user.setGender(gender);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(dateOfBirth);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		user.setDateOfBirth(date);
		
		
		return user;
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

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

}
