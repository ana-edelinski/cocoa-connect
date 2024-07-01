package services;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Factory;
import beans.Order;
import dao.FactoryDAO;
import dao.OrderDAO;
import dao.UserDAO;
import dto.FactoryWithChocolatesDto;
import enums.ChocolateKind;
import enums.ChocolateType;
@Path("/orders")
public class OrderService {
	@Context
	ServletContext ctx;

	public OrderService() {
	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("orderDao") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("orderDao", new FactoryDAO(contextPath));
		}
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> findAll() {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
		return dao.findAll();
	}

	

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Order findById(@PathParam("id") Integer id) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
		return dao.findById(id);
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Order update(@PathParam("id") Integer id, Order updatedOrder) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
		return dao.update(id, updatedOrder);
	}

	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Order save(Order order) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
		
		return dao.save(order);
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteFactory(@PathParam("id") Integer id) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
	}

	
	

	
}
