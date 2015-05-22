package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import dao.UserDAO;
import entity.User;

/**
 * Created by mds on 07/04/15.
 * Class ${CLASS}
 */
@Path("/user")
public class UserServices {
	private static final Logger LOGGER = Logger.getLogger(UserServices.class);

    private UserDAO dao;

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(User user) {
        int status = 401;
        User userData;
        if(user != null)
        {
            dao = new UserDAO();
            dao.connect();
            userData = dao.connectUser(user.getUsername(), user.getPassword());
            dao.disconnect();
            LOGGER.info("User " + user.getUsername());

            if (user.equals(userData)) {
                LOGGER.info("User " + userData.getUsername() + " connect");
                status = 200;
            } else {
                userData = new User();
            }
        }
        else
        {
            userData = new User();
        }
        return Response.status(status).entity(userData).build();
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

        return Response.status(200).entity("Hello " + username).build();
    }

}
