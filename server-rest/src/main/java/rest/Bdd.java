package rest;

import dao.UserDAO;
import entity.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

/**
 * Created by alban on 08/04/15.
 */
@Path("/bdd")
public class Bdd {
        @GET
        @Path("/init")
        public Response initBDD() {
            JSONParser parser = new JSONParser();
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(classLoader.getResource("init_bdd/user.json").getFile());
                Object obj = parser.parse(new FileReader(file));
                JSONArray users = (JSONArray) obj;
                Iterator<JSONObject> iterator = users.iterator();

                UserDAO userDAO = new UserDAO();
                userDAO.connect();
                while (iterator.hasNext()) {
                    JSONObject userJSON = iterator.next();
                    User tmpUser = new User();
                    tmpUser.setPassword((String) userJSON.get("password"));
                    tmpUser.setUsername((String) userJSON.get("username"));
                    userDAO.create(tmpUser);
                }
                // code qui n'a rien à voir avec le reste.
                // On crée le design document.
                userDAO.createDesignDocument();
                userDAO.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(502).build();
            }
            return Response.status(200).build();
        }
}
