package rest;


import dao.InterventionDAO;
import entity.Intervention;
import entity.Mean;
import entity.MeanState;
import entity.Position;
import org.apache.log4j.Logger;
import service.PushService.TypeClient;
import service.impl.PushServiceImpl;
import util.Datetime;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
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
    public synchronized Response validateMeanXtra(@PathParam("idintervention") long idintervention, Mean meanXtra) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        Boolean meanCanBeChanged = false;
        iD.connect();
        Intervention intervention = iD.getById(idintervention);

        for (int i=0; i<intervention.getMeansList().size(); i++) {
            if (intervention.getMeansList().get(i).getId() == meanXtra.getId() && intervention.getMeansList().get(i).getMeanState() == MeanState.REQUESTED) {
                try {
                    PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "ok", intervention.getMeansList().get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intervention.getMeansList().get(i).setDateActivated(Datetime.getCurrentDate());
                intervention.getMeansList().get(i).setMeanState(MeanState.ACTIVATED);
                meanCanBeChanged = true;
            }
        }


        if (meanCanBeChanged) {
            iD.update(intervention);
            iD.disconnect();
            return Response.status(200).entity("Mean is now available").build();
        }
        else {
            iD.disconnect();
            return Response.status(Response.Status.BAD_REQUEST).entity("Mean is already activated by Codis").build();
        }

    }


    @POST
    @Path("{idintervention}/nok")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response declineMeanXtra(@PathParam("idintervention") long idintervention,Mean meanXtra) {

        InterventionDAO iD = new InterventionDAO();
        Mean res = null;
        Boolean meanCanBeChanged = false;
        iD.connect();
        Intervention intervention = iD.getById(idintervention);

        for (Mean m : intervention.getMeansList()) {
            if (m.getId() == meanXtra.getId() && m.getMeanState() == MeanState.REQUESTED) {
                try {
                    PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "nok", m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                m.setDateRefused(Datetime.getCurrentDate());
                m.setMeanState(MeanState.REFUSED);
                meanCanBeChanged = true;
            }
        }

        if (meanCanBeChanged) {
            iD.update(intervention);
            iD.disconnect();
            return Response.status(200).entity("Mean was refused").build();
        }
        else {
            iD.disconnect();
            return Response.status(Response.Status.BAD_REQUEST).entity("Mean cannot be refused by Codis due to its current state").build();
        }
    }

}
