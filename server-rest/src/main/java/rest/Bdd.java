package rest;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dao.GeoIconDAO;
import dao.UserDAO;
import entity.GeoIcon;
import entity.Position;
import entity.User;

/**
 * Created by alban on 08/04/15.
 */
@Path("/bdd")
public class Bdd {
        @GET
        @Path("/init")
        public Response initBDD() {

         //   List<Long> idCreated = new ArrayList<Long>();
            // Ajout des users
            JSONParser parser = new JSONParser();
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File("./webapps/sitserver/init_bdd/user.json");
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
            // Ajout des icones
              parser = new JSONParser();
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File("./webapps/sitserver/init_bdd/icon.json");
                Object obj = parser.parse(new FileReader(file));
                JSONArray users = (JSONArray) obj;
                Iterator iterator = users.iterator();

                GeoIconDAO iconDAO = new GeoIconDAO();
                iconDAO.connect();
                while (iterator.hasNext()) {
                    JSONObject userJSON = (JSONObject) iterator.next();
                    GeoIcon tmpUser = new GeoIcon();
                    tmpUser.setEntitled((String) userJSON.get("entitled"));
                    tmpUser.setFilename((String) userJSON.get("filename"));
                   // tmpUser.setPosition(Tools.jsonArrayToPosition(userJSON.get("coordinates")));
                    JSONObject coordinates = (JSONObject) userJSON.get("coordinates");
                    Position position = new Position((Double) coordinates.get("longitude"),(Double) coordinates.get("latitude"));
                    tmpUser.setPosition(position);
                    tmpUser.setColor((String) userJSON.get("color"));
                    tmpUser.setTiret(Boolean.parseBoolean(""+userJSON.get("tiret")));
                    tmpUser.setFirstContent((String) userJSON.get("firstContent"));
                    tmpUser.setSecondContent((String) userJSON.get("secondContent"));

                    iconDAO.create(tmpUser);
                 //   idCreated.add(tmpUser.getId());
                }
                iconDAO.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(502).build();
            }
            return Response.status(200).entity("OK").build();
        }
}
