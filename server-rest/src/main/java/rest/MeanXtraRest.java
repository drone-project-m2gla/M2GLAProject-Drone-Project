package rest;


import dao.InterventionDAO;
import entity.Intervention;
import entity.Mean;
import entity.Position;
import org.apache.log4j.Logger;
import service.PushService.TypeClient;
import service.impl.PushServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * Created by arno on 12/02/15.
 *
 * Service rest du type intervention
 */
@Path("/moyen")
public class MeanXtraRest {

    private static final Logger LOGGER = Logger.getLogger(MeanXtraRest.class);

    @POST
    @Path("{idintervention}/ok")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response validateMeanXtra(@PathParam("idintervention") long idintervention, Mean meanXtra) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(idintervention);

        LOGGER.debug("Nbre elements à valider : "+intervention.getMeansXtra().size());
        for (int i=0; i<intervention.getMeansXtra().size(); i++) {
            LOGGER.debug(intervention.getMeansXtra().get(i).getId()+ " - " + intervention.getMeansXtra().get(i).getVehicle()+ " - " + intervention.getMeansXtra().get(i).getisDeclined());
        }

        for (int i=0; i<intervention.getMeansXtra().size(); i++) {
            if (intervention.getMeansXtra().get(i).getId() == meanXtra.getId()) {
                try {
                    PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "ok", intervention.getMeansXtra().get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                try {
                    PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "nok", m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        iD.update(intervention);

        iD.disconnect();
        return Response.status(200).entity("Mean was refused").build();
    }
}
