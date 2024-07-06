package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Chocolate;
import beans.Comment;
import beans.Factory;
import beans.WorkingHours;
import dto.FactoryWithChocolatesDto;
import enums.FactoryStatus;

public class FactoryDAO {
	private HashMap<Integer, Factory> factories = new HashMap<>();
    public static String contextPath;
    
    private static FactoryDAO instance;
    
    private FactoryDAO(String contextPath) {
		loadFactories(contextPath);
    }
    
    public static FactoryDAO getInstance() {
    	if(instance == null) {
    		instance = new FactoryDAO(contextPath);
    	}
    	return instance;
    }
	public Collection<Factory> findAll() {
		Collection<Factory> result = new ArrayList<>();
		for (var c : factories.values()) {
			if (!c.isDeleted())
				result.add(c);
		}
		return result;
	}

	public Factory findByManagerId(int managerId) {
		for (var c : factories.values()) {
			if (c.getManagerId() == managerId)
				return c;
		}
		return null;
	}

	public Collection<FactoryWithChocolatesDto> findAllWithChocolates() {
		ArrayList<FactoryWithChocolatesDto> allFactoriesWith = new ArrayList<FactoryWithChocolatesDto>();

		ChocolateDAO chocolateDAO = ChocolateDAO.getInstance();

		for (Factory factory : findAll()) {
			ArrayList<Chocolate> chocolates = new ArrayList<Chocolate>(chocolateDAO.getAllForFactory(factory.getId()));

			FactoryWithChocolatesDto chocolatesDto = new FactoryWithChocolatesDto(factory);
			chocolatesDto.setChocolates(chocolates);
			allFactoriesWith.add(chocolatesDto);

		}

		return allFactoriesWith;
	}

	public Factory findById(int id) {
		return factories.containsKey(id) ? factories.get(id) : null;
	}

	public Factory getFactoryForManager(int managerId) {
		for (Factory factory : findAll()) {
			if (factory.getManagerId() == managerId) {
				return factory;
			}
		}
		return null;
	}

	public Factory update(int id, Factory factory) {
		Factory f = factories.containsKey(id) ? factories.get(id) : null;
		if (f == null) {
			return save(factory);
		} else {
			f.setName(factory.getName());
			f.setAverageRating(factory.getAverageRating());
			f.setCity(factory.getCity());
			f.setCountry(factory.getCountry());
			f.setAverageRating(factory.getAverageRating());
			f.setLogo(factory.getLogo());
			f.setWorkingHours(factory.getWorkingHours());
			f.setComment(factory.getComment());
			f.setManagerId(factory.getManagerId());
		}
		saveToFile(contextPath);
		return f;
	}

	public Factory save(Factory factory) {
		Integer maxId = -1;
		for (Integer id : factories.keySet()) {
			int idNum = id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		factory.setId(maxId);
		factories.put(factory.getId(), factory);
		saveToFile(contextPath);
		return factory;
	}

	private void loadFactories(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/factories.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", name = "", factoryStatus = "", city = "", country = "", averageRating = "",
					deleted = "", logo = "", workingHours = "", comment = "", managerId = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					name = st.nextToken().trim();
					factoryStatus = st.nextToken().trim();
					city = st.nextToken().trim();
					country = st.nextToken().trim();
					averageRating = st.nextToken().trim();
					deleted = st.nextToken().trim();
					logo = st.nextToken().trim();
					workingHours = st.nextToken().trim();
					comment = st.nextToken().trim();
					managerId = st.nextToken().trim();
				}
				String[] hours = workingHours.split("-");
				WorkingHours wh = new WorkingHours(LocalTime.parse(hours[0]), LocalTime.parse(hours[1]));
				factories.put(Integer.parseInt(id),
						new Factory(Integer.parseInt(id), name, factoryStatus, city, country,
								Double.parseDouble(averageRating), Boolean.parseBoolean(deleted), logo, wh, comment,
								Integer.parseInt(managerId)));
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
			File file = new File(path + "/factories.csv");
			out = new BufferedWriter(new FileWriter(file));

			for (Factory factory : factories.values()) {
				StringBuilder line = new StringBuilder();
				line.append(factory.getId()).append(";");
				line.append(factory.getName()).append(";");
				line.append(factory.getFactoryStatus().toString()).append(";");
				line.append(factory.getCity()).append(";");
				line.append(factory.getCountry()).append(";");
				line.append(factory.getAverageRating()).append(";");
				line.append(factory.isDeleted()).append(";");
				line.append(factory.getLogo()).append(";");
				line.append(factory.getWorkingHours().getStartTime().toString()).append("-");
				line.append(factory.getWorkingHours().getEndTime().toString()).append(";");
				line.append(factory.getComment()).append(";");
				line.append(factory.getManagerId());
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

	public Factory deleteFactory(int id) {
		Factory factory = findById(id);
		if (factory == null)
			return null;
		factory.setDeleted(true);
		return saveToFile(contextPath) ? factory : null;
	}

	public Collection<FactoryWithChocolatesDto> searchFactories(String name, String chocolateName, String location,
			Double minRating, Double maxRating) {
		ArrayList<FactoryWithChocolatesDto> allFactoriesWith = new ArrayList<>();

		ChocolateDAO chocolateDAO = ChocolateDAO.getInstance();

		for (Factory factory : findAll()) {

			boolean matchesName = (name == null || name.isEmpty())
					|| factory.getName().toLowerCase().contains(name.toLowerCase());
			boolean matchesLocation = (location == null || location.isEmpty())
					|| factory.getCity().toLowerCase().contains(location.toLowerCase())
					|| factory.getCountry().toLowerCase().contains(location.toLowerCase());
			boolean matchesMinRating = (minRating == null) || factory.getAverageRating() >= minRating;
			boolean matchesMaxRating = (maxRating == null) || factory.getAverageRating() <= maxRating;

			ArrayList<Chocolate> chocolates = new ArrayList<>(chocolateDAO.getAllForFactory(factory.getId()));

			boolean matchesChocolateName = (chocolateName == null || chocolateName.isEmpty()) || chocolates.stream()
					.anyMatch(chocolate -> chocolate.getName().toLowerCase().contains(chocolateName.toLowerCase()));

			if (matchesName && matchesLocation && matchesMinRating && matchesMaxRating && matchesChocolateName) {
				FactoryWithChocolatesDto chocolatesDto = new FactoryWithChocolatesDto(factory);
				chocolatesDto.setChocolates(chocolates);
				allFactoriesWith.add(chocolatesDto);
			}
		}

		return allFactoriesWith;
	}

	public Collection<FactoryWithChocolatesDto> filterFactories(String chocolateType, String chocolateCategory,
			Boolean openOnly) {
		ArrayList<FactoryWithChocolatesDto> allFactoriesWith = new ArrayList<>();

		ChocolateDAO chocolateDAO = ChocolateDAO.getInstance();

		for (Factory factory : findAll()) {
			boolean matchesOpenStatus;
			if (openOnly == null) {
				matchesOpenStatus = true;
			} else if (openOnly) {
				matchesOpenStatus = factory.getFactoryStatus() == FactoryStatus.OPENED;
			} else {
				matchesOpenStatus = factory.getFactoryStatus() == FactoryStatus.CLOSED;
			}

			ArrayList<Chocolate> chocolates = new ArrayList<>(chocolateDAO.getAllForFactory(factory.getId()));

			boolean matchesChocolateType = (chocolateType == null || chocolateType.isEmpty())
					|| chocolates.stream().anyMatch(chocolate -> chocolate.getType().toString().equals(chocolateType));
			boolean matchesChocolateKind = (chocolateCategory == null || chocolateCategory.isEmpty()) || chocolates
					.stream().anyMatch(chocolate -> chocolate.getKind().toString().equals(chocolateCategory));

			if (matchesOpenStatus && matchesChocolateType && matchesChocolateKind) {
				FactoryWithChocolatesDto chocolatesDto = new FactoryWithChocolatesDto(factory);
				chocolatesDto.setChocolates(chocolates);
				allFactoriesWith.add(chocolatesDto);
			}
		}

		return allFactoriesWith;
	}

	public Collection<FactoryWithChocolatesDto> sortFactories(String sortBy, String order) {
		ArrayList<FactoryWithChocolatesDto> sortedFactories = new ArrayList<>(findAllWithChocolates());

		sortedFactories.sort((a, b) -> {
			int comparison = 0;
			switch (sortBy.toLowerCase()) {
			case "name":
				comparison = a.getName().compareToIgnoreCase(b.getName());
				break;
			case "location":
				comparison = (a.getCity() + a.getCountry()).compareToIgnoreCase(b.getCity() + b.getCountry());
				break;
			case "rating":
				comparison = Double.compare(a.getAverageRating(), b.getAverageRating());
				break;
			}
			return "desc".equalsIgnoreCase(order) ? -comparison : comparison;
		});

		return sortedFactories;
	}
	
	public void updateAverageRating(int factoryId) {
	    Factory factory = factories.get(factoryId);
	    if (factory != null) {
	        double totalRating = 0;
	        int numberOfRatings = 0;
	        CommentDAO commentDAO = CommentDAO.getInstance();
	        for (Comment comment : commentDAO.getAllApprovedForFactory(factoryId)) {
	            System.out.println("Rating: " + comment.getGrade());  // Log each rating
	            totalRating += comment.getGrade();
	            numberOfRatings++;
	        }
	        double averageRating = numberOfRatings == 0 ? 0 : totalRating / numberOfRatings;
	        System.out.println("New Average Rating: " + averageRating);  // Log the new average rating
	        factory.setAverageRating(averageRating);
	        saveToFile(contextPath);  // Ensure this method persists changes
	    }
	}


}
