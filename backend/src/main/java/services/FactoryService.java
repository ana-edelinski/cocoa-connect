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

import beans.Factory;
import dao.FactoryDAO;
import dao.UserDAO;

@Path("/factories")
public class FactoryService {
    @Context
    ServletContext ctx;
    
    public FactoryService() {}
    
    @PostConstruct
    public void init() {
        if (ctx.getAttribute("factoryDao") == null) {
            String contextPath = ctx.getRealPath("");
            ctx.setAttribute("factoryDao", new FactoryDAO(contextPath));
        }
        if (ctx.getAttribute("userDao") == null) {
            String contextPath = ctx.getRealPath("");
            ctx.setAttribute("userDao", new UserDAO(contextPath));
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
        userDao.setManagerAssigned(factory.getManagerId());
        
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
}
