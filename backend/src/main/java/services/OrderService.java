package services;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Factory;
import beans.Order;
import dao.FactoryDAO;
import dao.OrderDAO;
import dto.CartDto;
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
			ctx.setAttribute("orderDao", new OrderDAO(contextPath));
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
	@Path("/forManagersFactory/{managerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> getforManagersFacotry(@PathParam("managerId") Integer id) {
		FactoryDAO factoryDAO = new FactoryDAO(ctx.getRealPath(""));
		Factory factory = factoryDAO.findByManagerId(id);
		
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
		return dao.findAllByFactory(factory.getId());
	}
	
	
	@GET
	@Path("/byUserId/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> getAllByUser(@PathParam("id") Integer id) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
		return dao.findAllByUser(id);
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
	@Path("/cancel/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Order cancel(@PathParam("id") Integer id, Order updatedOrder) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
		Order order = dao.cancel(id);
		return order;
	}

	@PUT
	@Path("/aprove/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Order aprove(@PathParam("id") Integer id, Order updatedOrder) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
		Order order = dao.aprove(id);
		return order;
	}
	@PUT
	@Path("/reject/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Order reject(@PathParam("id") Integer id, Map<String, String> data) {
	    String comment = data.get("comment");
	    OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
	    Order order = dao.reject(id, comment);
	    return order;
	}
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Order save(CartDto cartDto) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
		Order order = dao.saveCart(cartDto);
		
		
		return order;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteFactory(@PathParam("id") Integer id) {
		OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
	}

	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Order> searchOrders(@Context javax.ws.rs.core.UriInfo uriInfo) {
	    Map<String, String> queryParams = uriInfo.getQueryParameters().keySet().stream()
	            .collect(java.util.stream.Collectors.toMap(key -> key, key -> uriInfo.getQueryParameters().getFirst(key)));

	    String factoryName = queryParams.getOrDefault("factoryName", "");
	    double minPrice = queryParams.containsKey("minPrice") ? Double.parseDouble(queryParams.get("minPrice")) : 0;
	    double maxPrice = queryParams.containsKey("maxPrice") ? Double.parseDouble(queryParams.get("maxPrice")) : 0;
	    LocalDate startDate = queryParams.containsKey("startDate") ? LocalDate.parse(queryParams.get("startDate")) : null;
	    LocalDate endDate = queryParams.containsKey("endDate") ? LocalDate.parse(queryParams.get("endDate")) : null;

	    OrderDAO dao = (OrderDAO) ctx.getAttribute("orderDao");
	    return dao.searchOrders(factoryName, minPrice, maxPrice, startDate, endDate);
	}


	
	
	
}
