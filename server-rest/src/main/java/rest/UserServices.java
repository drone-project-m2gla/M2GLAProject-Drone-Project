package rest;

import dao.AbstractDAO;
import dao.UserDAO;
import entity.AbstractEntity;
import entity.User;

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
        dao = new UserDAO();
        AbstractEntity user = new User();
        String response = "";
        return Response.status(200).entity("Hello " + username).build();
    }

    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @POST
    @Path("create")
    public Response createUser(@FormParam("username") String username, @FormParam("password") String password) {
        dao = new UserDAO();
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);

        dao.connect();
        dao.create(user);
        dao.disconnect();

        String response = "";
        return Response.status(200).entity("Hello " + username).build();
    }

}
