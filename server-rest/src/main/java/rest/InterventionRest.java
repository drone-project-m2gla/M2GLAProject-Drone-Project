package rest;


import dao.InterventionDAO;
import entity.Intervention;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by arno on 12/02/15.
 */
@Path("/intervention")
public class InterventionRest {

    @GET
    @Path("{id}")
    public Response getPosition(@PathParam("id") long id) {

        return Response.status(200).entity("Intervention id is : " + id).build();

    }

    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response setIntervention(Intervention intervention) {
        InterventionDAO iD = new InterventionDAO();
        iD.connect();
        Intervention res= iD.create(intervention);
        iD.disconnect();
        return Response.status(200).entity("L'intervation : " + res.getId() + "<BR>" + res.toString()).build();
    }
//
//    @POST
//    @Path("zoneObject")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public Response getPositionObject(GeoInterventionZone zone) {
//        System.out.println("LA zone\t" + zone);
//        String coordinatesZone = "Zone de survol";
//        Zone flyoverZone = zone.getCoordinates().get(0);
//        Iterator<Position> it = flyoverZone.positionIterator();
//        while (it.hasNext()) {
//            Position p = it.next();
//            coordinatesZone += "<BR>Latitude " + p.getLatitude();
//            coordinatesZone += " / Longitude " + p.getLongitude();
//            coordinatesZone += "/ Altitude " + p.getAltitude();
//        }
//        return Response.status(200).entity("Le nom de la zone est : " + zone + "<BR>" + coordinatesZone).build();
//    }
//
//    @POST
//    @Path("position")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public Response getPositionObject(Position zone) {
//        return Response.status(200).entity("Le nom de la zone est : " + zone.getLatitude()).build();
//    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Hello Jersey";
    }


}
