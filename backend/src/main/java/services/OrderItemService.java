package services;

import java.util.Collection;

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

import beans.Order;
import beans.OrderItem;
import dao.FactoryDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
@Path("/items")
public class OrderItemService {
	@Context
	ServletContext ctx;

	public OrderItemService() {
	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("itemDao") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("itemDao", new OrderItemDAO(contextPath));
		}
	}

	

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<OrderItem> findAll() {
		OrderItemDAO dao = (OrderItemDAO) ctx.getAttribute("itemDao");
		return dao.findAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public OrderItem findById(@PathParam("id") Integer id) {
		OrderItemDAO dao = (OrderItemDAO) ctx.getAttribute("itemDao");
		return dao.findById(id);
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public OrderItem update(@PathParam("id") Integer id, OrderItem updatedItem) {
		OrderItemDAO dao = (OrderItemDAO) ctx.getAttribute("itemDao");
		return dao.update(id, updatedItem);
	}

	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public OrderItem save(OrderItem item) {
		OrderItemDAO dao = (OrderItemDAO) ctx.getAttribute("itemDao");
		
		return dao.save(item);
	}
	
	@GET
	@Path("/forOrder/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<OrderItem> getItemsForOrder(@PathParam("orderId") Integer orderId) {
	    OrderItemDAO dao = (OrderItemDAO) ctx.getAttribute("itemDao");
	    return dao.getItemsForOrder(orderId);
	}
	
}
