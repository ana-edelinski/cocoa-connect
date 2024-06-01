package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Chocolate;
import beans.Factory;

public class FactoryDAO {
	private HashMap<String, Factory> factories = new HashMap<String, Factory>();
    private String contextPath;
    
    public FactoryDAO() {}
    
    public FactoryDAO(String contextPath) {
    	this.contextPath = contextPath;
		loadProducts(contextPath);
    }
    
    public Collection<Factory> findAll() {
        Collection<Factory> retVal = new ArrayList();
        
        for(var c : factories.values()) {
        	if(!c.isDeleted()) 		
        		retVal.add(c);
        }
        
        return retVal;
    }
    
    public Factory findById(String id) {
		return factories.containsKey(id) ? factories.get(id) : null;
	}
    
    public Factory update(String id, Factory factory) {
		Factory f = factories.containsKey(id) ? factories.get(id) : null;
		if (f == null) {
			return save(factory);
		} else {
			f.setName(factory.getName());
			f.setAverageRating(factory.getAverageRating());
			f.setCity(factory.getCity());
			f.setCountry(factory.getCountry());
			f.setAverageRating(factory.getAverageRating());
			
		}
		
		return f;
	}
    
    public Factory save(Factory factory) {
		Integer maxId = -1;
		for (String id : factories.keySet()) {
			int idNum =Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		factory.setId(maxId.toString());
		factories.put(factory.getId(), factory);
		return factory;
	}
    
    private void loadProducts(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/factories.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", name = "",factoryStatus = "",city = "", country = "",averageRating = "",deleted = "" ;
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
					
					deleted =  st.nextToken().trim();

				}
				factories.put(id, new Factory(id, name,factoryStatus,city,country,Double.parseDouble(averageRating), Boolean.parseBoolean(deleted)));
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

    private boolean saveToFile(String path) {
        BufferedWriter out = null;
        try {
            File file = new File(path + "/factories.csv");
            out = new BufferedWriter(new FileWriter(file));


            for (Factory factory : factories.values()) {
                StringBuilder line = new StringBuilder();
                line.append(factory.getId()).append(";");
                line.append(factory.getName()).append(";");
                line.append(factory.getAverageRating()).append(";");
                line.append(factory.getCity()).append(";");
                line.append(factory.getCountry()).append(";");
                line.append(factory.getAverageRating()).append(";");
                line.append(factory.isDeleted());
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
    public Factory deleteChocolate(String id) {
		Factory factory = findById(id);
		if(factory == null) return null;
		factory.setDeleted(false);
		return saveToFile(contextPath) ? factory : null;
	}  
}
