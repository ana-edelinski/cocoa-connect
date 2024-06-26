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
import dto.LoginDto;
import dto.SearchCriteriaDTO;
import dto.UserCreationDto;
import dto.UserDto;
import enums.ChocolateStatus;
import enums.Role;

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
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserCreationDto userDto) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
        try {
        	User user = userDto.convertToUser();
        	user.setRole(Role.CUSTOMER);
        	user.setIsAssigned(false);
            user = dao.save(user);
            
            UserDto returnDto = new UserDto(user);
            return Response.status(Response.Status.CREATED).entity(returnDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDto loginDto) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
        try {
        	User user = dao.findByUsername(loginDto.getUsername());
        	if(user == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
        	}
        	if(!user.getPassword().equals(loginDto.getPassword())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
        	}
        	UserDto dto = new UserDto(user);
            return Response.status(Response.Status.CREATED).entity(dto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Path("/register-manager")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerManager(UserCreationDto userDto) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
        try {
            User user = userDto.convertToUser();
            user.setRole(Role.MANAGER);
            user.setIsAssigned(false);
            user = dao.save(user);
            
            UserDto returnDto = new UserDto(user);
            return Response.status(Response.Status.CREATED).entity(returnDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Path("/employees")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEmployee(UserCreationDto userDto) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
        try {
            User user = userDto.convertToUser();
            user.setRole(Role.EMPLOYEE); 
            user.setIsAssigned(true); 
                        
            user = dao.save(user); 
                
            UserDto returnDto = new UserDto(user);
            return Response.status(Response.Status.CREATED).entity(returnDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<User> searchUsers(SearchCriteriaDTO criteria) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
        return dao.searchUsers(criteria);
    }

}
