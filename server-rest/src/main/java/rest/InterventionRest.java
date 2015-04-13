package rest;


import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.InterventionDAO;
import entity.Intervention;
import entity.Mean;
import entity.Position;
import service.PushService.TypeClient;
import service.impl.PushServiceImpl;

/**
 * Created by arno on 12/02/15.
 *
 * Service rest du type intervention
 */
@Path("/intervention")
public class InterventionRest {



    @GET
    @Path("/{id}/moyen/{idmean}")
    @Produces({MediaType.APPLICATION_JSON})
    public Mean getMeanForIntervention(@PathParam("id") long id,@PathParam("idmean") long idmean) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        iD.connect();
        List<Mean> meanList = iD.getById(id).getMeansList();
        iD.disconnect();

        for (Mean mean : meanList) {
            if (mean.getId() == idmean) {
                res = mean;
            }
        }

        return res;

    }

    @POST
    @Path("/{id}/moyen/enplace")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Mean validateMeanPositionForIntervention(@PathParam("id") long id, Mean mean) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(id);
        List<Mean> meanList = intervention.getMeansList();

        for (Mean m : meanList) {
            if (m.getId() == mean.getId()) {
                m.setInPosition(true);
                res = m;
            }
        }
        iD.update(intervention);
        iD.disconnect();
        return res;

    }


    @POST
    @Path("/{id}/moyen/positionner")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Mean updateMeanPositionForIntervention(@PathParam("id") long id, Mean mean) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(id);
        List<Mean> meanList = intervention.getMeansList();

        for (Mean m : meanList) {
            if (m.getId() == mean.getId()) {
                m.setCoordinates(mean.getCoordinates());
                m.setInPosition(false);
                res = m;
            }
        }
        iD.update(intervention);
        iD.disconnect();
        return res;

    }

    @POST
    @Path("/{id}/moyenextra")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Mean getMeanListForIntervention(@PathParam("id") long id,Mean meanXtra) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(id);
        intervention.getMeansXtra().add(meanXtra);

        iD.update(intervention);

        iD.disconnect();

        try {
            PushServiceImpl.getInstance().sendMessage(TypeClient.CODIS, "xtra",intervention);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return intervention.getMeansXtra().get(intervention.getMeansXtra().size()-1);
    }

    @GET
    @Path("/{id}/moyen")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Mean> getMeanListForIntervention(@PathParam("id") long id) {

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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Intervention setIntervention(Intervention intervention) {
        InterventionDAO iD = new InterventionDAO();
        iD.connect();

        // Code temporaire a remplacé par service google pour retrouver les coordonnées GPS
        Position p1 = new Position(Double.NaN,Double.NaN);
        intervention.setCoordinates(p1);

        // Génération de la liste des moyens
        intervention.generateMeanList();
        Intervention res = iD.create(intervention);
        iD.disconnect();

        return res;
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



}
