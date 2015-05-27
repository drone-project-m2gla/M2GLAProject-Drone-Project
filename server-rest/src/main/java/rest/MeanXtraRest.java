package rest;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.InterventionDAO;
import entity.Intervention;
import entity.Mean;
import entity.MeanState;
import service.PushService.TypeClient;
import service.impl.PushServiceImpl;
import util.Datetime;

/**
 * @author arno on 12/02/15.
 * @see MeanXtraRest is service rest of type intervention
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
        iD.connect();
        Intervention intervention = iD.getById(idintervention);

        for (Mean m : intervention.getMeansList()) {
            if (m.getId() == meanXtra.getId() && m.getMeanState() == MeanState.REQUESTED) {
                m.setDateActivated(Datetime.getCurrentDate());
                m.setMeanState(MeanState.ACTIVATED);
                m.setName(meanXtra.getName());
                res = m;
            }
        }

        if (res != null) {
            iD.update(intervention);
            iD.disconnect();

            try {
                PushServiceImpl.getInstance().sendMessage(TypeClient.ALL, "ok", res);
            } catch (IOException e) {
                LOGGER.error("", e);
            }

            return Response.status(200).entity("Mean is now available").build();
        } else {
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
        iD.connect();
        Intervention intervention = iD.getById(idintervention);

        for (Mean m : intervention.getMeansList()) {
            if (m.getId() == meanXtra.getId() && m.getMeanState() == MeanState.REQUESTED) {
                m.setDateRefused(Datetime.getCurrentDate());
                m.setMeanState(MeanState.REFUSED);
                res = m;
            }
        }

        if (res != null) {
            iD.update(intervention);
            iD.disconnect();

            try {
                PushServiceImpl.getInstance().sendMessage(TypeClient.ALL, "nok", res);
            } catch (IOException e) {
                LOGGER.error("", e);
            }

            return Response.status(200).entity("Mean was refused").build();
        } else {
            iD.disconnect();
            return Response.status(Response.Status.BAD_REQUEST).entity("Mean cannot be refused by Codis due to its current state").build();
        }
    }
}
