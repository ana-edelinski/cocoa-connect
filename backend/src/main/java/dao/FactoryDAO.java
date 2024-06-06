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

import beans.Factory;
import beans.WorkingHours;

public class FactoryDAO {
    private HashMap<Integer, Factory> factories = new HashMap<>();
    private String contextPath;

    public FactoryDAO() {}

    public FactoryDAO(String contextPath) {
        this.contextPath = contextPath;
        loadFactories(contextPath);
    }

    public Collection<Factory> findAll() {
        Collection<Factory> result = new ArrayList<>();
        for (var c : factories.values()) {
            if (!c.isDeleted())
                result.add(c);
        }
        return result;
    }

    public Factory findById(int id) {
        return factories.containsKey(id) ? factories.get(id) : null;
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
            f.setComment(factory.getComment()); // Update comment field
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
            String line, id = "", name = "", factoryStatus = "", city = "", country = "", averageRating = "", deleted = "", logo = "", workingHours = "", comment = "";
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
                    comment = st.nextToken().trim(); // Read comment
                }
                String[] hours = workingHours.split("-");
                WorkingHours wh = new WorkingHours(LocalTime.parse(hours[0]), LocalTime.parse(hours[1]));
                factories.put(Integer.parseInt(id), new Factory(Integer.parseInt(id), name, factoryStatus, city, country, Double.parseDouble(averageRating), Boolean.parseBoolean(deleted), logo, wh, comment));
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
                line.append(factory.getComment()); // Append comment
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
        if (factory == null) return null;
        factory.setDeleted(true);
        return saveToFile(contextPath) ? factory : null;
    }
}
