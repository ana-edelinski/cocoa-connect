package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.User;
import enums.Gender;
import enums.Role;

public class UserDAO {
	private HashMap<Integer, User> users = new HashMap<>();
	private String contextPath;

	public UserDAO() {
	}

	public UserDAO(String contextPath) {
		this.contextPath = contextPath;
		loadUsers(contextPath);
	}

	public Collection<User> findAll() {
		return users.values();
	}

	public User findById(int id) {
		return users.containsKey(id) ? users.get(id) : null;
	}

	public User findByUsername(String username) {

		for (User user : users.values()) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	public User update(int id, User user) {
		User u = users.containsKey(id) ? users.get(id) : null;
		if (u == null) {
			return save(user);
		} else {
			u.setUsername(user.getUsername());
			u.setPassword(user.getPassword());
			u.setName(user.getName());
			u.setSurname(user.getSurname());
			u.setGender(user.getGender());
			u.setDateOfBirth(user.getDateOfBirth());
			u.setRole(user.getRole());
			u.setIsAssigned(user.getIsAssigned());
		}
		saveToFile(contextPath);
		return u;
	}

	public User save(User user) {
		int maxId = -1;
		for (Integer id : users.keySet()) {
			int idNum = id;
			if (id > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		user.setId(maxId);
		users.put(user.getId(), user);
		saveToFile(contextPath);
		return user;
	}

	private void loadUsers(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/users.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.startsWith("#"))
					continue;

				StringTokenizer st = new StringTokenizer(line, ";");

				String id = st.nextToken().trim();
				String username = st.nextToken().trim();
				String password = st.nextToken().trim();
				String name = st.nextToken().trim();
				String surname = st.nextToken().trim();
				Gender gender = Gender.valueOf(st.nextToken().trim());
				Date dateOfBirth = sdf.parse(st.nextToken().trim());
				Role role = Role.valueOf(st.nextToken().trim());
				boolean isAssigned = Boolean.parseBoolean(st.nextToken().trim());

				users.put(Integer.parseInt(id), new User(Integer.parseInt(id), username, password, name, surname,
						gender, dateOfBirth, role, isAssigned));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean saveToFile(String path) {
		BufferedWriter out = null;
		try {
			File file = new File(path + "/users.csv");
			out = new BufferedWriter(new FileWriter(file));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (User user : users.values()) {
				StringBuilder line = new StringBuilder();
				line.append(user.getId()).append(";");
				line.append(user.getUsername()).append(";");
				line.append(user.getPassword()).append(";");
				line.append(user.getName()).append(";");
				line.append(user.getSurname()).append(";");
				line.append(user.getGender().toString()).append(";");
				line.append(sdf.format(user.getDateOfBirth())).append(";");
				line.append(user.getRole().toString()).append(";");
				line.append(user.getIsAssigned());
				out.write(line.toString());
				out.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	public User deleteUser(int id) {
		User user = findById(id);
		if (user == null)
			return null;
		users.remove(id);
		return saveToFile(contextPath) ? user : null;
	}

	public Collection<User> findAvailableManagers() {
		Collection<User> availableManagers = new ArrayList<>();
		for (User user : users.values()) {
			if (user.getRole() == Role.MANAGER && !user.getIsAssigned()) {
				availableManagers.add(user);
			}
		}
		return availableManagers;
	}

	public void setManagerAssigned(int managerId) {
		User manager = users.get(managerId);
		if (manager != null) {
			manager.setIsAssigned(true);
			users.put(managerId, manager);
		}
	}
}
