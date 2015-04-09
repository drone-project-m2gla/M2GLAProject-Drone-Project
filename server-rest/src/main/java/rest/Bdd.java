package rest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import dao.GeoIconDAO;
import dao.UserDAO;
import entity.GeoIcon;
import entity.User;

/**
 * Created by alban on 08/04/15.
 */
@Path("/bdd")
public class Bdd {
        @GET
        @Path("/init")
        public Response initBDD() {

            // Ajout des users
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
            // Ajout des icones
              parser = new JSONParser();
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(classLoader.getResource("init_bdd/icon.json").getFile());
                Object obj = parser.parse(new FileReader(file));
                JSONArray users = (JSONArray) obj;
                Iterator<JSONObject> iterator = users.iterator();

                GeoIconDAO iconDAO = new GeoIconDAO();
                iconDAO.connect();
                while (iterator.hasNext()) {
                    JSONObject userJSON = iterator.next();
                    GeoIcon tmpUser = new GeoIcon();
                    tmpUser.setEntitled((String) userJSON.get("entitled"));
                    tmpUser.setFilename((String) userJSON.get("filename"));
                    iconDAO.create(tmpUser);
                }
                iconDAO.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(502).build();
            }
            return Response.status(200).build();
        }
}
