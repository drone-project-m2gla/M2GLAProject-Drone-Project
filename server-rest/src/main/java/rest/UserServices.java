package rest;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.*;
import dao.UserDAO;
import entity.User;
import util.Constant;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mds on 07/04/15.
 * Class ${CLASS}
 */
@Path("/user")
public class UserServices {

    private UserDAO dao;

    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @POST
    @Path("login")
    public Response logIn(@FormParam("username") String username, @FormParam("password") String password) {
        dao = new UserDAO();
        dao.connect();
        User user = dao.getByUsername(username);

        String success = "ERROR";

        int status = 401;
        if (user != null) {
            System.out.println("User\t" + user);

            String passwd = user.getPassword();
            if (passwd != null && passwd.equals(password)) {
                status = 200;
                success = username;
            }
        }
        dao.disconnect();
        return Response.status(status).entity(success).build();
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

    @GET
    public Response findAll() {
        dao = new UserDAO();
        dao.connect();
        Bucket bucket = dao.currentBucket;
        JsonDocument doc = bucket.get("mdiansow");
        System.out.println("User doc\t" + doc);
        System.out.println("bucket\t" + bucket.name());
        dao.disconnect();
        return Response.status(200).entity(doc).build();
    }

}
