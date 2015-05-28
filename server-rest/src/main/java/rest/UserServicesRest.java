package rest;

import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.UserDAO;
import entity.User;

/**
 * @author mds on 07/04/15.
 * @see UserServicesRest is using for connection
 */
@Path("/user")
public class UserServicesRest {
	private static final Logger LOGGER = Logger.getLogger(UserServicesRest.class);

    private UserDAO dao;

    /**
     * logIn verify if users exists or not
     * @param user
     * @return Response
     */
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

    /**
     * create a user
     * @param username
     * @param password
     * @return Response
     */
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
