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
import beans.Chocolate;
import dao.ChocolateDAO;

@Path("/chocolates")
public class ChocolateService {

    @Context
    ServletContext ctx;

    public ChocolateService() {}

    @PostConstruct
    public void init() {
        if (ctx.getAttribute("chocolateDAO") == null) {
            String contextPath = ctx.getRealPath("");
            ctx.setAttribute("chocolateDAO", new ChocolateDAO(contextPath));
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Chocolate> findAll() {
        ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        return dao.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Chocolate findById(@PathParam("id") Integer id) {
        ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        return dao.findById(id);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Chocolate update(@PathParam("id") Integer id, Chocolate updatedChocolate) {
        ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        return dao.update(id, updatedChocolate);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Chocolate save(Chocolate chocolate) {
        ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        if(!dao.validateChocolate(chocolate)) {
        	return null;
        }
        return dao.save(chocolate);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteChocolate(@PathParam("id") Integer id) {
        ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        dao.deleteChocolate(id);
    }

    @GET
    @Path("/getAllForFactory/{factoryId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Collection<Chocolate> getAllForFactory(@PathParam("factoryId") Integer factoryId) {
        ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
        return dao.getAllForFactory(factoryId);
    }
}
