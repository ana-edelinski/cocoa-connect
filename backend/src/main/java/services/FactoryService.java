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
import javax.ws.rs.core.Response;

import beans.Factory;
import dao.CommentDAO;
import dao.FactoryDAO;
import dao.UserDAO;
import dto.FactoryWithChocolatesDto;
import enums.ChocolateKind;
import enums.ChocolateType;

@Path("/factories")
public class FactoryService {
	@Context
	ServletContext ctx;

	public FactoryService() {
	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("factoryDao") == null) {
			String contextPath = ctx.getRealPath("");
			DaosStartUp.initDaos(contextPath);
            ctx.setAttribute("factoryDao", FactoryDAO.getInstance());
		}
		if (ctx.getAttribute("userDao") == null) {
			String contextPath = ctx.getRealPath("");
			DaosStartUp.initDaos(contextPath);
            ctx.setAttribute("userDao", UserDAO.getInstance());
		}
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Factory> findAll() {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDao");
		return dao.findAll();
	}

	@GET
	@Path("/withChocolates")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<FactoryWithChocolatesDto> findAllWithChocolates() {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDao");
		return dao.findAllWithChocolates();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Factory findById(@PathParam("id") Integer id) {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDao");
		return dao.findById(id);
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Factory update(@PathParam("id") Integer id, Factory updatedFactory) {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDao");
		return dao.update(id, updatedFactory);
	}

	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Factory save(Factory factory) {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDao");
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDao");
		Factory savedFactory = dao.save(factory);

		// Set manager as assigned
		userDao.setManagerAssigned(factory.getManagerId(), savedFactory.getId());

		return savedFactory;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteFactory(@PathParam("id") Integer id) {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDao");
		dao.deleteFactory(id);
	}

	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<FactoryWithChocolatesDto> searchFactories(@QueryParam("name") String name,
			@QueryParam("chocolateName") String chocolateName, @QueryParam("location") String location,
			@QueryParam("minRating") Double minRating, @QueryParam("maxRating") Double maxRating) {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDao");
		return dao.searchFactories(name, chocolateName, location, minRating, maxRating);
	}

	@GET
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<FactoryWithChocolatesDto> filterFactories(@QueryParam("chocolateType") String chocolateType,
			@QueryParam("chocolateKind") String chocolateKind, @QueryParam("openOnly") Boolean openOnly) {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDao");
		return dao.filterFactories(chocolateType, chocolateKind, openOnly);
	}

	@GET
	@Path("/sort")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<FactoryWithChocolatesDto> sortFactories(@QueryParam("sortBy") String sortBy,
			@QueryParam("order") String order) {
		FactoryDAO dao = (FactoryDAO) ctx.getAttribute("factoryDao");
		return dao.sortFactories(sortBy, order);
	}

	@GET
	@Path("/chocolateTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getChocolateTypes() {
		return Arrays.stream(ChocolateType.values()).map(Enum::name).collect(Collectors.toList());
	}

	@GET
	@Path("/chocolateKinds")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getChocolateKinds() {
		return Arrays.stream(ChocolateKind.values()).map(Enum::name).collect(Collectors.toList());
	}
	
	@GET
    @Path("/manager/{managerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Factory getFactoryForManager(@PathParam("managerId") int managerId) {
        FactoryDAO factoryDao = (FactoryDAO) ctx.getAttribute("factoryDao");
        if (factoryDao == null) {
            factoryDao = FactoryDAO.getInstance();
            ctx.setAttribute("factoryDao", factoryDao);
        }
        return factoryDao.getFactoryForManager(managerId);
    }
	
	@GET
    @Path("/byManager/{managerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFactoryByManager(@PathParam("managerId") Integer managerId) {
        FactoryDAO factoryDAO = FactoryDAO.getInstance();
        Factory factory = factoryDAO.findByManagerId(managerId);
        if (factory != null) {
            return Response.ok(factory).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }



}
