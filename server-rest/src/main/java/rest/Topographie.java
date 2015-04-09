package rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import dao.GeoIconDAO;
import entity.GeoIcon;

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
         * @param
         * @return
         */

        @GET
        @Path("{Long}/{Latitude}/{Rayon}")
       // @Produces(MediaType.APPLICATION_JSON)
        public List<GeoIcon> getCoordinatedIcons(@PathParam("Long") float positionLongitude, @PathParam("Latitude") float positionLatitude, @PathParam("Rayon") Long rayon) {

          //Position position = new Position(-1.667, 48.100);
          //return Response.status(200).entity("Longitude :" +positionLongitude+ "Latitude"+positionLatitude+"Rayon "+rayon ).build();

            GeoIconDAO gID = new GeoIconDAO();
            gID.connect();
            List<GeoIcon> res = gID.getAll();
            gID.disconnect();
            System.out.println(res);
            return res;
        }
}