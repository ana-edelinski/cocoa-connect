package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Chocolate;

public class ChocolateDAO {
    private HashMap<String, Chocolate> chocolates = new HashMap<String, Chocolate>();
    
    public ChocolateDAO() {}
    
    public ChocolateDAO(String contextPath) {
		loadProducts(contextPath);
    }
    
    public Collection<Chocolate> findAll() {
        return chocolates.values();
    }
    
    public Chocolate findById(String id) {
		return chocolates.containsKey(id) ? chocolates.get(id) : null;
	}
    
    public Chocolate update(String id, Chocolate chocolate) {
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
		for (String id : chocolates.keySet()) {
			int idNum =Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		chocolate.setId(maxId.toString());
		chocolates.put(chocolate.getId(), chocolate);
		return chocolate;
	}
    
    private void loadProducts(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/chocolates.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", name = "", price = "", kind = "", factory = "", type = "", weight = "", description = "", image = "", status = "", quantity = "";
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

				}
				chocolates.put(id, new Chocolate(id, name, Double.parseDouble(price), kind, factory, type, Integer.parseInt(weight), description, image, status, Integer.parseInt(quantity)));
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

    public boolean deleteChocolate(String id) {
		return chocolates.remove(id) != null;
	}  
}
