package services;

import dao.ChocolateDAO;
import dao.CommentDAO;
import dao.FactoryDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.UserDAO;

public class DaosStartUp {

	public static void initDaos(String contextPath) {
		ChocolateDAO.contextPath = contextPath;
		CommentDAO.contextPath = contextPath;
		FactoryDAO.contextPath = contextPath;
		OrderDAO.contextPath = contextPath;
		OrderItemDAO.contextPath = contextPath;
		UserDAO.contextPath = contextPath;
		//za ostale
		
		
		
		//za ostale
		UserDAO.getInstance();
		ChocolateDAO.getInstance();
		CommentDAO.getInstance();
		FactoryDAO.getInstance();
		OrderDAO.getInstance();
		OrderItemDAO.getInstance();
	}
}
