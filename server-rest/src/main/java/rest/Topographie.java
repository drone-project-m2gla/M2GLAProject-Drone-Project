package rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dao.CoordinatedIconDAO;
import entity.CoordinatedIcon;
import entity.Position;

/**
 * Created by jerem on 08/04/15.
 *
 * Envoie  l'ensemble des icons
 */
@Path("/topographie")
public class Topographie {


    /**
     * Retourner une liste json de points statiques sur la carte autour de la GeoPosition précisée en paramètre.
     * Par défaut, on peut prendre une valeur de 50km autour.
     * @param aGeoPosition
     * @return
     */
        @GET
        @Path("{GeoPosition}")
        @Produces(MediaType.APPLICATION_JSON)
        public List<CoordinatedIcon> getCoordinatedIcons(@PathParam("GeoPosition") Position aGeoPosition) {

            CoordinatedIconDAO gID = new CoordinatedIconDAO();
            gID.connect();
            List<CoordinatedIcon> res = gID.getAll();
            gID.disconnect();
            return res;

          //  return Response.status(200).entity("Le drone id est : " + aGeoPosition).build();

        }


}
