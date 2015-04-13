package rest;


import dao.InterventionDAO;
import entity.Intervention;
import entity.Mean;
import entity.Position;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by arno on 12/02/15.
 *
 * Service rest du type intervention
 */
@Path("/moyen")
public class MeanXtraRest {

    @POST
    @Path("{idintervention}/ok")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response validateMeanXtra(@PathParam("idintervention") long idintervention, Mean meanXtra) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(idintervention);

        for (int i=0; i<=intervention.getMeansXtra().size(); i++) {
            if (intervention.getMeansXtra().get(i).getId() == meanXtra.getId()) {
                intervention.getMeansList().add(intervention.getMeansXtra().get(i));
                intervention.getMeansXtra().remove(intervention.getMeansXtra().get(i));
            }
        }

        iD.update(intervention);
        iD.disconnect();
        return Response.status(200).entity("Mean is now available").build();
    }


    @POST
    @Path("{idintervention}/nok")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response declineMeanXtra(@PathParam("idintervention") long idintervention,Mean meanXtra) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(idintervention);


        for (Mean m : intervention.getMeansXtra()) {
            if (m.getId() == meanXtra.getId()) {
                m.setisDeclined(true);
            }
        }

        iD.update(intervention);

        iD.disconnect();
        return Response.status(200).entity("Mean "+meanXtra.getVehicle().name()+" was refused").build();
    }
}
