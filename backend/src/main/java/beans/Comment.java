package beans;

import enums.CommentStatus;

public class Comment {
	private int id;
	private User user;
	private Factory factory;
	private Order order;
	private String text;
	private int grade;
	private CommentStatus status;
}

