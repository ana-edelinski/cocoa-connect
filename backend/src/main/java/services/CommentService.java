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
import javax.ws.rs.core.Response;

import beans.Comment;
import dao.CommentDAO;
import dao.FactoryDAO;
import dto.CommentCreationDto;

@Path("/comments")
public class CommentService {
	@Context
	ServletContext ctx;

	public CommentService() {
	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("commentDAO") == null) {
			String contextPath = ctx.getRealPath("");
			DaosStartUp.initDaos(contextPath);
			ctx.setAttribute("commentDAO", CommentDAO.getInstance());
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> findAll() {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.findAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment findById(@PathParam("id") Integer id) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.findById(id);
	}
	
	@GET
	@Path("/getAllForFactory/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Comment> getAllForFactory(@PathParam ("factoryId") int factoryId) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.getAllForFactory(factoryId);
	}
	
	@GET
	@Path("/getAllApprovedForFactory/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<Comment> getAllForFactory(@PathParam("factoryId") Integer factoryId) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		return dao.getAllApprovedForFactory(factoryId);
	}
	@PUT
	@Path("/aprove/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment aprove(@PathParam("id") Integer id) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		Comment comment = dao.aprove(id);
		return comment;
	}
	@PUT
	@Path("/reject/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment reject(@PathParam("id") Integer id) {
	    CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
	    Comment comment = dao.reject(id);
	    return comment;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(CommentCreationDto commentCreationDto) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");

		Comment comment = dao.getCommentForOrder(commentCreationDto.getOrderId());
		if (comment != null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		Comment createdComment = dao.save(commentCreationDto);
		if(createdComment == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		return Response.status(Response.Status.CREATED).entity(createdComment).build();

	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteComment(@PathParam("id") Integer id) {
		CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
		dao.deleteComment(id);
	}
	
	@POST
    @Path("/submitFeedback")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitFeedback(CommentCreationDto commentCreationDto) {
        CommentDAO commentDAO = CommentDAO.getInstance();
        FactoryDAO factoryDAO = FactoryDAO.getInstance();

        Comment comment = commentDAO.save(commentCreationDto);
        if (comment != null) {
            factoryDAO.updateAverageRating(comment.getFactory().getId());
            return Response.ok(comment).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("You have already commented on this order.").build();
        }
    }


}
