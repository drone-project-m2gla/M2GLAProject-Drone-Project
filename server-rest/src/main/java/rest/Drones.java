package rest;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by arno on 12/02/15.
 */
@Path("/drones")
public class Drones {

    @GET
    //@Path("")
    //  @Consumes({ MediaType.APPLICATION_JSON })
    public Response getDrones(String drones) {

        return Response.status(200).entity("Les drones sont : " + drones).build();

    }

    @GET
    @Path("{id}")
    //  @Consumes({ MediaType.APPLICATION_JSON })
    public Response getDronesById(@PathParam("id") int id) {

        return Response.status(200).entity("Le drone id est : " + id).build();

    }

    @POST
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response postDronesById(@PathParam("id") int id) {

        return Response.status(200).entity("Le drone id est : " + id).build();

    }

}
