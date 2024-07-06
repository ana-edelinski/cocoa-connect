package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
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
import beans.User;
import dao.FactoryDAO;
import dao.UserDAO;
import dto.ChangePasswordDto;
import dto.EmployeeCreationDto;
import dto.LoginDto;
import dto.UserCreationDto;
import dto.UserDto;
import enums.Role;

@Path("/users")
public class UserService {
	@Context
	ServletContext ctx;

	public UserService() {
	}

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
			if (user == null) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			if (!user.getPassword().equals(loginDto.getPassword())) {
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
			//
			user.setPoints(0);
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
	public Response addEmployee(EmployeeCreationDto employeDto) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
		FactoryDAO factoryDAO = new FactoryDAO(ctx.getRealPath(""));
		try {
			Factory factory = factoryDAO.findByManagerId(employeDto.getManagerId());
			
			User user = employeDto.convertToUser();
			user.setIsAssigned(false);
			user.setFactoryWorkingId(factory.getId());
			
			user = dao.save(user);

			UserDto returnDto = new UserDto(user);
			return Response.status(Response.Status.CREATED).entity(returnDto).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> searchUsers(@QueryParam("name") String name, @QueryParam("surname") String surname,
			@QueryParam("username") String username) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
		return dao.searchUsers(name, surname, username);
	}

	@GET
	@Path("/sort")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> sortUsers(@QueryParam("sortBy") String sortBy, @QueryParam("order") String order) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
		return dao.sortUsers(sortBy, order);
	}

	@GET
	@Path("/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> filterUsers(@QueryParam("role") String role) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
		return dao.filterUsers(role);
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@PathParam("id") int id) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
		User user = dao.findById(id);
		if (user != null) {
			return Response.ok(new UserDto(user)).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User update(@PathParam("id") Integer id, User updatedUser) {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDao");
		// todo: Validacija
		return dao.update(id, updatedUser);
	}

	@PUT
	@Path("/changePassword/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(@PathParam("userId") int userId, ChangePasswordDto passwordDto) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDao");
		User user = userDao.findById(userId);

		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		if (!user.getPassword().equals(passwordDto.getCurrentPassword())) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		user.setPassword(passwordDto.getNewPassword());
		userDao.update(userId, user);

		return Response.ok().build();
	}

}