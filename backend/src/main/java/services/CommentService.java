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

import beans.Comment;
import dao.CommentDAO;
@Path("/comments")
public class CommentService {
	@Context
    ServletContext ctx;

    public CommentService() {}

    @PostConstruct
    public void init() {
        if (ctx.getAttribute("commentDAO") == null) {
            String contextPath = ctx.getRealPath("");
            ctx.setAttribute("commentDAO", new CommentDAO(contextPath));
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

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Comment update(@PathParam("id") Integer id, Comment updatedComment) {
    	CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
        return dao.update(id, updatedComment);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Comment save(Comment comment) {
    	CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
        return dao.save(comment);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteComment(@PathParam("id") Integer id) {
    	CommentDAO dao = (CommentDAO) ctx.getAttribute("commentDAO");
        dao.deleteComment(id);
    }

  
}
