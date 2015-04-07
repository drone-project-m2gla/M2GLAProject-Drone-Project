package rest;


import dao.GeoInterventionZoneDAO;
import entity.GeoInterventionZone;
import entity.Position;
import entity.Zone;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Iterator;

/**
 * Created by arno on 12/02/15.
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
    @Path("setzone")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response setGeoInterventionZone(GeoInterventionZone zone) {
        GeoInterventionZoneDAO gIZD = new GeoInterventionZoneDAO();
        gIZD.connect();
        GeoInterventionZone res= gIZD.create(zone);
        gIZD.disconnect();
        return Response.status(200).entity("Le nom de la zone est : " + zone + "<BR>" + res.toString()).build();
    }

    @POST
    @Path("zoneObject")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getPositionObject(GeoInterventionZone zone) {
        System.out.println("LA zone\t" + zone);
        String coordinatesZone = "Zone de survol";
        Zone flyoverZone = zone.getCoordinates().get(0);
        Iterator<Position> it = flyoverZone.positionIterator();
        while (it.hasNext()) {
            Position p = it.next();
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
