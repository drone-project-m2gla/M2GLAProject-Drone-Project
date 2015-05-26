package rest;


import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import entity.GeoInterventionZone;
import entity.Position;
import entity.Zone;

/**
 * @author arno on 12/02/15.
 */
@Path("/geoposition")
public class GeoPosition {

    @GET
    @Path("{latitude}/{longitude}")
    public Response getPosition(@PathParam("latitude") float latitude, @PathParam("longitude") float longitude) {

        return Response.status(200).entity("Coordonates are latitude: " + latitude + " /  longitude" + longitude).build();

    }

    @POST
    @Path("zone")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getPosition(String zone) {

        return Response.status(200).entity("La zone est : " + zone).build();

    }


    @POST
    @Path("point")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getPoint(String point) {

        return Response.status(200).entity("Le point est : " + point).build();

    }

    @POST
    @Path("zoneObject")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getPositionObject(GeoInterventionZone zone) {
        String coordinatesZone = "Zone de survol";
        Zone flyoverZone = zone.getCoordinates().get(0);
        Iterator it = flyoverZone.positionIterator();
        while (it.hasNext()) {
            Position p = (Position) it.next();
            coordinatesZone += "<BR>Latitude " + p.getLatitude();
            coordinatesZone += " / Longitude " + p.getLongitude();
            coordinatesZone += "/ Altitude " + p.getAltitude();
        }


//        for (List<Double> z : zone.getCoordinates().get(0)) {
//            coordinatesZone = coordinatesZone + "<BR>Latitude" + z.get(0) + " / Longitude"+z.get(1);
//        }
        return Response.status(200).entity("Le nom de la zone est : " + zone + "<BR>" + coordinatesZone).build();
    }

    @POST
    @Path("position")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getPositionObject(Position zone) {
        return Response.status(200).entity("Le nom de la zone est : " + zone.getLatitude()).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Hello Jersey";
    }


}
