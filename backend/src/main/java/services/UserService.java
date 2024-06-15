package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.User;
import dao.UserDAO;

@Path("/users")
public class UserService {
    @Context
    ServletContext ctx;
    
    public UserService() {}
    
    @PostConstruct
    public void init() {
        if (ctx.getAttribute("userDao") == null) {
            String contextPath = ctx.getRealPath("");
            ctx.setAttribute("userDao", new UserDAO(contextPath));
        }
    }
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<User> findAll() {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
        return dao.findAll();
    }
    
    @GET
    @Path("/available-managers")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<User> findAvailableManagers() {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
        return dao.findAvailableManagers();
    }
}
