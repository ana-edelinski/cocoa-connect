package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EnumType;

import beans.Chocolate;
import beans.Comment;
import beans.Factory;
import beans.Order;
import beans.User;
import enums.CommentStatus;
import enums.Role;

public class CommentDAO {
	private HashMap<Integer, Comment> comments = new HashMap<Integer, Comment>();
    public String contextPath;
    
    public CommentDAO() {}
    
    public CommentDAO(String contextPath) {
		loadComments(contextPath);
		this.contextPath = contextPath;
    }
    
    public Collection<Comment> findAll() {
    	Collection<Comment> result = new ArrayList();
        
        for(var c : comments.values()) { 		
        		result.add(c);
        }
        
        return result;
    }
    
    public Comment findById(int id) {
		return comments.containsKey(id) ? comments.get(id) : null;
	}
    
    public Comment update(int id, Comment comment) {
		Comment c = comments.containsKey(id) ? comments.get(id) : null;
		if (c == null) {
			return save(comment);
		} else {
			c.setUser(comment.getUser());
			c.setFactory(comment.getFactory());
			c.setOrder(comment.getOrder());
			c.setText(comment.getText());
			c.setGrade(comment.getGrade());
			c.setStatus(comment.getStatus());
			saveToFile(contextPath);
		}
		
		return c;
	}
    
    public Comment save(Comment comment) {
		Integer maxId = -1;
		for (Integer id : comments.keySet()) {
			int idNum =id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		comment.setId(maxId);
		//chocolate.setImage("../resources/" + chocolate.getImage());
		comments.put(comment.getId(), comment);
		saveToFile(contextPath);
		return comment;
	}
    
    
    
    private void loadComments(String contextPath) {
		BufferedReader in = null;
		
		UserDAO userDAO = new UserDAO(contextPath);
		FactoryDAO factoryDAO = new FactoryDAO(contextPath);
		OrderDAO orderDao = new OrderDAO(contextPath);
		try {
			File file = new File(contextPath + "/chocolates.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", userId = "", factoryId = "", orderId = "", text = "", grade = "", status = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					userId = st.nextToken().trim();
					factoryId = st.nextToken().trim();
					orderId = st.nextToken().trim();
					text = st.nextToken().trim();
					grade = st.nextToken().trim();
					status = st.nextToken().trim();
				}
				
				int idC = Integer.parseInt(id);
				int userIdInt = Integer.parseInt(userId);
				int factoryIdInt = Integer.parseInt(factoryId);
				int orderIdInt = Integer.parseInt(orderId);
				User user = userDAO.findById(userIdInt);
				Factory factory = factoryDAO.findById(factoryIdInt);
				Order order = orderDao.findById(orderIdInt);
				int gradeI = Integer.parseInt(grade);
				CommentStatus statusP = CommentStatus.valueOf(st.nextToken().trim());
				comments.put(Integer.parseInt(id), new Comment(idC, user,factory, order, text, gradeI,statusP ));
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

    public boolean deleteComment(int id) {
		Comment c = findById(id);
		if(c == null) return false;
	
		return saveToFile(contextPath);
    }  
    
    
    private boolean saveToFile(String path) {
        BufferedWriter out = null;
        try {
            File file = new File(path + "chocolates.csv");
            out = new BufferedWriter(new FileWriter(file));

            for (Comment comment : comments.values()) {
                StringBuilder line = new StringBuilder();
                line.append(comment.getId()).append(";");
                line.append(comment.getUser()).append(";");
                line.append(comment.getFactory()).append(";");
                line.append(comment.getOrder()).append(";");
                line.append(comment.getText()).append(";");
                line.append(comment.getGrade()).append(";");
                line.append(comment.getStatus());
                
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
