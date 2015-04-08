package rest;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.view.*;
import dao.UserDAO;
import entity.AbstractEntity;
import entity.User;
import org.json.JSONArray;
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
    public Response longIn(@FormParam("username") String username, @FormParam("password") String password) {
        dao = new UserDAO();
        dao.connect();
        AbstractEntity user = new User();

        Bucket currentBucket = dao.currentBucket;

        JSONArray array = new JSONArray();

        System.out.println("**********************");
        System.setProperty("viewmode", "development"); // before the connection to Couchbase
        try {
            DesignDocument designDoc = currentBucket.bucketManager().getDesignDocument("user_login");
            if (designDoc == null) {
                // Create user design doc.
                userDesingDoc(currentBucket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create connection if needed

        ViewQuery query = ViewQuery.from("user_login", "login_view");

        ViewResult result = currentBucket.query(query);

        for (ViewRow row : result) {
            System.out.println(row.document()); // deal with the document/data
        }

        System.out.println("************************");


        System.out.println("Les Documents******\t" + result.totalRows());

//        String response = String.valueOf(dao.findByLogin(username, password));
        dao.disconnect();
        return Response.status(200).entity("Hello " + username).entity(array.toString()).build();
    }

    private void userDesingDoc(Bucket currentBucket) {
        String view = "function(doc) {\n" +
                "  if(doc.properties.datatype == \"USER\"){\t\n" +
                "       \temit([doc.properties.username, doc.properties.password], doc.properties.username);\n" +
                "  }\n" +
                "}";
        List<View> viewArray = new ArrayList<View>();
        viewArray.add(DefaultView.create("login_view", view));
        DesignDocument user_design = DesignDocument.create("user_login", viewArray);
        currentBucket.bucketManager().insertDesignDocument(user_design);
    }

    private void loginView() {
        dao = new UserDAO();
        dao.connect();
        Bucket currentBucket = dao.currentBucket;

        DesignDocument designDoc = currentBucket.bucketManager().getDesignDocument("designDoc");

        String dataType = Constant.DATATYPE_USER;

        // Perform the ViewQuery
        ViewResult result = currentBucket.query(ViewQuery.from("dev_user", "user_login"));

        // Iterate through the returned ViewRows
        for (ViewRow row : result) {
            System.out.println(row);
        }

//                "function(doc) {\n" +
//                        "  if(doc.datatype == \"USER\"){\t\n" +
//                        "       \temit([doc.username, doc.password], doc.username);\n" +
//                        "  }\n" +
//                        "}";

        dao.disconnect();
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
