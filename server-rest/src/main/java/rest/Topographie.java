package rest;

import javax.ws.rs.Path;

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
        /*

        @GET
        @Path("{GeoPosition}/{Rayon}")
       // @Produces(MediaType.APPLICATION_JSON)
        public void getCoordinatedIcons(@PathParam("GeoPosition") Position aGeoPosition) {

            CoordinatedIconDAO gID = new CoordinatedIconDAO();
            gID.connect();
            List<CoordinatedIcon> res = gID.getAll();
            gID.disconnect();
            return Response.status(200).entity(res).build();


        }
*/

}
