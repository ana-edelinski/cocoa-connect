package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
        try {
            User createdUser = dao.save(user);
            if (createdUser != null) {
                return Response.status(Response.Status.CREATED).entity(createdUser).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
