package rest;


import dao.InterventionDAO;
import entity.Intervention;
import entity.Mean;
import entity.Position;
import entity.Zone;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by arno on 12/02/15.
 *
 * Service rest du type intervention
 */
@Path("/intervention")
public class InterventionRest {



    @GET
    @Path("/{id}/moyen")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Mean> getMeanForIntervention(@PathParam("id") long id) {

        InterventionDAO iD = new InterventionDAO();
        iD.connect();
        List<Mean> res = iD.getById(id).getMeansList();
        iD.disconnect();
        return res;

    }


    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Intervention getIntervention(@PathParam("id") long id) {

        InterventionDAO iD = new InterventionDAO();
        iD.connect();
        Intervention res = iD.getById(id);
        System.out.println("-------------->"+res);
        iD.disconnect();
        return res;

    }

    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Intervention> getAllIntervention() {

        InterventionDAO iD = new InterventionDAO();
        iD.connect();
        List<Intervention> res = iD.getAll();
        iD.disconnect();
        return res;

    }

    @POST
    @Path("")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response setIntervention(Intervention intervention) {
        InterventionDAO iD = new InterventionDAO();
        iD.connect();

        // Code temporaire a remplacé par service google pour retrouver les coordonnées GPS
        Position p1 = new Position(-1,-1);
        intervention.setCoordinates(p1);

        // Génération de la liste des moyens
        intervention.generateMeanList();
        Intervention res = iD.create(intervention);
        iD.disconnect();

        return Response.status(200).entity(""+res.getId()).build();
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
