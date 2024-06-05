package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.Path;

import beans.Chocolate;
import beans.Factory;
import enums.ChocolateStatus;

public class ChocolateDAO {
    private HashMap<Integer, Chocolate> chocolates = new HashMap<Integer, Chocolate>();
    public String contextPath;
    
    public ChocolateDAO() {}
    
    public ChocolateDAO(String contextPath) {
		loadProducts(contextPath);
		this.contextPath = contextPath;
    }
    
    public Collection<Chocolate> findAll() {
    	Collection<Chocolate> result = new ArrayList();
        
        for(var c : chocolates.values()) {
        	if(!c.isDeleted()) 		
        		result.add(c);
        }
        
        return result;
    }
    
    public Chocolate findById(int id) {
		return chocolates.containsKey(id) ? chocolates.get(id) : null;
	}
    
    public Chocolate update(int id, Chocolate chocolate) {
		Chocolate c = chocolates.containsKey(id) ? chocolates.get(id) : null;
		if (c == null) {
			return save(chocolate);
		} else {
			c.setName(chocolate.getName());
			c.setPrice(chocolate.getPrice());
			c.setKind(chocolate.getKind());
			c.setFactory(chocolate.getFactory());
			c.setType(chocolate.getType());
			c.setWeight(chocolate.getWeight());
			c.setDescription(chocolate.getDescription());
			c.setImage(chocolate.getImage());
			c.setStatus(chocolate.getStatus());
			c.setQuantity(chocolate.getQuantity());
		}
		
		return c;
	}
    
    public Chocolate save(Chocolate chocolate) {
		Integer maxId = -1;
		for (Integer id : chocolates.keySet()) {
			int idNum =id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		chocolate.setId(maxId);
		chocolate.setImage("../resources/" + chocolate.getImage());
		chocolates.put(chocolate.getId(), chocolate);
		saveToFile(contextPath);
		return chocolate;
	}
    
    public boolean validateChocolate(Chocolate chocolate) {
    	if(chocolate == null) {
    		return false;
    	}
    	if(chocolate.getName() == null || chocolate.getName().equals("")) {
    		return false;
    	}
    	if(chocolate.getPrice() <= 0) {
    		return false;
    	}
    	if(chocolate.getKind() == null) {
    		return false;
    	}
    	if(chocolate.getType() == null) {
    		return false;
    	}
    	if(chocolate.getFactory() <= 0) {
    		return false;
    	}
    	if(chocolate.getWeight() <= 0) {
    		return false;
    	}
    	if(chocolate.getDescription().equals("")) {
    		return false;
    	}
    	if(chocolate.getImage().equals("") || chocolate.getImage() == null) {
    		return false;
    	}
    	if(chocolate.getStatus() == null) {
    		return false;
    	}
    	
    	return true;
    }
    
    private void loadProducts(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/chocolates.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", name = "", price = "", kind = "", factory = "", type = "", weight = "", description = "", image = "", status = "", quantity = "",deleted="";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					name = st.nextToken().trim();
					price = st.nextToken().trim();
					kind = st.nextToken().trim();
					factory = st.nextToken().trim();
					type = st.nextToken().trim();
					weight = st.nextToken().trim();
					description = st.nextToken().trim();
					image = st.nextToken().trim();
					status = st.nextToken().trim();
					quantity = st.nextToken().trim();
					deleted = st.nextToken().trim();
				}
				chocolates.put(Integer.parseInt(id), new Chocolate(Integer.parseInt(id), name, Double.parseDouble(price), kind, Integer.parseInt(factory), type, Integer.parseInt(weight), description, image, Integer.parseInt(quantity), Boolean.parseBoolean(deleted)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}

    public boolean deleteChocolate(int id) {
		Chocolate c = findById(id);
		if(c == null) return false;
		c.setDeleted(true);
		return saveToFile(contextPath);
    }  
    
    public Collection<Chocolate> getAllForFactory(Integer factoryId) {
        List<Chocolate> result = new ArrayList<>();
        for (Chocolate chocolate : findAll()) {
            if (chocolate.getFactory() ==  factoryId) {
                result.add(chocolate);
            }
        }
        return result;
    }
    private boolean saveToFile(String path) {
        BufferedWriter out = null;
        try {
            File file = new File(path + "chocolates.csv");
            out = new BufferedWriter(new FileWriter(file));

            for (Chocolate chocolate : chocolates.values()) {
                StringBuilder line = new StringBuilder();
                line.append(chocolate.getId()).append(";");
                line.append(chocolate.getName()).append(";");
                line.append(chocolate.getPrice()).append(";");
                line.append(chocolate.getKind()).append(";");
                line.append(chocolate.getFactory()).append(";");
                line.append(chocolate.getType()).append(";");
                line.append(chocolate.getWeight()).append(";");
                line.append(chocolate.getDescription()).append(";");
                line.append(chocolate.getImage()).append(";");
                line.append(chocolate.getStatus()).append(";");
                line.append(chocolate.getQuantity()).append(";");
                line.append(chocolate.isDeleted());
                out.write(line.toString());
                out.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
