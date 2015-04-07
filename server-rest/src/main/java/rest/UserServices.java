package rest;

import dao.AbstractDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by mds on 07/04/15.
 * Class ${CLASS}
 */
@Path("/user")
public class UserServices {

    private AbstractDAO dao;

    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @POST
    @Path("login")
    public Response longIn(@FormParam("username") String username, @FormParam("password") String password) {
        String response = "";
        return Response.status(200).entity("Hello " + username).build();
    }
}
