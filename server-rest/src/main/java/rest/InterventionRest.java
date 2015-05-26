package rest;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
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
import entity.MeanState;
import entity.Position;
import service.PushService.TypeClient;
import service.impl.PushServiceImpl;
import service.impl.RetrieveAddressImpl;
import util.Datetime;

/**
 * Service rest du type intervention
 * @author arno on 12/02/15
 */
@Path("/intervention")
public class InterventionRest {
	private static final Logger LOGGER = Logger.getLogger(InterventionRest.class);
    /**
     * @param id for the intervention
     * @param idmean for the id of the mean
     * @return Mean for a specific intervention
     */
	@GET
	@Path("/{id}/moyen/{idmean}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMeanForIntervention(@PathParam("id") long id,@PathParam("idmean") long idmean) {

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
        if(res!=null)
        {
            return Response.ok(res).build();
        }
        return Response.noContent().build();
	}

    /**
     * Add mean to intervention
     */
	@POST
	@Path("/{id}/moyen/emplace")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public synchronized Response validateMeanPositionForIntervention(@PathParam("id") long id, Mean mean) {
		InterventionDAO iD = new InterventionDAO();
        Boolean positionChanged = false;
		Mean res = null;
		iD.connect();
		Intervention intervention = iD.getById(id);
		List<Mean> meanList = intervention.getMeansList();

		for (Mean m : meanList) {
			if (m.getId() == mean.getId()) {
                if (m.getMeanState() == MeanState.ENGAGED) {
                    m.setInPosition(true);
                    res = m;
                    positionChanged = true;
                    break;
                }
			}
		}

        if (positionChanged) {
            iD.update(intervention);
            iD.disconnect();
            try {
                PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "moyenValide", res);
            } catch (IOException e) {
                LOGGER.error("Error push service intervention", e);
            }
            return Response.ok(res).build();
        }
        else
        {
            iD.disconnect();
            return Response.status(Response.Status.BAD_REQUEST).entity("Already in position or mean unavailable").build();
        }
	}
    /**
     * Update the position of this Intervention's mean
     */
	@POST
	@Path("/{id}/moyen/positionner")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public synchronized Response updateMeanPositionForIntervention(@PathParam("id") long id, Mean mean) {
		InterventionDAO iD = new InterventionDAO();
		Mean res = null;
        Boolean meanPositionAsked = false;
		iD.connect();
		Intervention intervention = iD.getById(id);
		List<Mean> meanList = intervention.getMeansList();

		for (Mean m : meanList) {
			if (m.getId() == mean.getId()) {
                if (m.getMeanState() == MeanState.ARRIVED || m.getMeanState() == MeanState.ENGAGED) {
                    m.setCoordinates(mean.getCoordinates());
                    m.setMeanState(MeanState.ENGAGED);
                    m.setInPosition(false);
                    if (m.getDateEngaged() == null) {
                        m.setDateEngaged(Datetime.getCurrentDate());
                    }
                    meanPositionAsked = true;
                    res = m;
                    break;
                }
			}
		}

        if (meanPositionAsked) {
            iD.update(intervention);
            iD.disconnect();
            try {
                PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "moyenMove", res);
            } catch (IOException e) {
                LOGGER.error("Error push service intervention", e);
            }
            return Response.ok(res).build();
        }
        else
        {
            iD.disconnect();
            return Response.status(Response.Status.BAD_REQUEST).entity("Mean cannot be engaged yet").build();
        }

	}
    /**
     * Confirmation that Intervention's mean is arrived
     */
    @POST
    @Path("/{id}/moyen/arrive")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response confirmMeanArrivalForIntervention(@PathParam("id") long id, Mean mean) {
        InterventionDAO iD = new InterventionDAO();
        Boolean meanArrived = false;
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(id);
        List<Mean> meanList = intervention.getMeansList();

        for (Mean m : meanList) {
            if (m.getId() == mean.getId()) {
                if (m.getMeanState() == MeanState.ACTIVATED) {
                    m.setDateArrived(Datetime.getCurrentDate());
                    m.setMeanState(MeanState.ARRIVED);
                    res = m;
                    meanArrived = true;
                    break;
                }
            }
        }

        if (meanArrived) {
            iD.update(intervention);
            iD.disconnect();
            try {
                PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "moyenArrive", res);
            } catch (IOException e) {
                LOGGER.error("Error push service intervention", e);
            }
            return Response.ok(res).build();
        }
        else
        {
            iD.disconnect();
            return Response.status(Response.Status.BAD_REQUEST).entity("Mean is already arrived or not yet activated").build();
        }
    }

    @POST
    @Path("/{id}/moyen/retourcrm")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response sendMeanBackToCRMForIntervention(@PathParam("id") long id, Mean mean) {
        InterventionDAO iD = new InterventionDAO();
        Boolean meanEngaged = false;
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(id);
        List<Mean> meanList = intervention.getMeansList();

        for (Mean m : meanList) {
            if (m.getId() == mean.getId() && (m.getMeanState() == MeanState.ENGAGED)) {
                m.setCoordinates(mean.getCoordinates());
                m.setMeanState(MeanState.ARRIVED);
                m.setInPosition(false);
                res = m;
                meanEngaged = true;
                break;
            }
        }

        if (meanEngaged) {
            iD.update(intervention);
            iD.disconnect();
            try {
                PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "moyenAuCRM", res);
            } catch (IOException e) {
                LOGGER.error("Error push service intervention", e);
            }
            return Response.ok(res).build();
        }
        else
        {
            iD.disconnect();
            return Response.status(Response.Status.BAD_REQUEST).entity("Mean is not engaged").build();
        }
    }

    @POST
    @Path("/{id}/moyen/libere")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response releaseMeanForIntervention(@PathParam("id") long id, Mean mean) {
        InterventionDAO iD = new InterventionDAO();
        Boolean meanReleased = false;
        Mean res = null;
        iD.connect();
        Intervention intervention = iD.getById(id);
        List<Mean> meanList = intervention.getMeansList();

        for (Mean m : meanList) {
            if (m.getId() == mean.getId() &&
                       (m.getMeanState() == MeanState.ACTIVATED
                     || m.getMeanState() == MeanState.ENGAGED
                     || m.getMeanState() == MeanState.ARRIVED)) {
                m.setCoordinates(mean.getCoordinates());
                m.setDateArrived(Datetime.getCurrentDate());
                m.setMeanState(MeanState.RELEASED);
                m.setInPosition(false);
                res = m;
                meanReleased = true;
                break;
            }
        }

        if (meanReleased) {
            iD.update(intervention);
            iD.disconnect();
            try {
                PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "moyenLibere", res);
            } catch (IOException e) {
                LOGGER.error("Error push service intervention", e);
            }
            return Response.ok(res).build();
        }
        else
        {
            iD.disconnect();
            return Response.status(Response.Status.BAD_REQUEST).entity("Mean is already released or not in a state where it can be released").build();
        }
    }


	@POST
	@Path("/{id}/moyenextra")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public synchronized Response addExtraMeanToIntervention(@PathParam("id") long id,Mean meanXtra) {
		InterventionDAO iD = new InterventionDAO();
		iD.connect();
		Intervention intervention = iD.getById(id);
		intervention.getMeansList().add(meanXtra);

		iD.update(intervention);

		iD.disconnect();

		try {
			PushServiceImpl.getInstance().sendMessage(TypeClient.ALL, "xtra", intervention);
		} catch (IOException e) {
			LOGGER.error("Error push service intervention", e);
		}

		return Response.ok(intervention.getMeansList().get(intervention.getMeansList().size()-1)).build();
	}

	@GET
	@Path("/{id}/moyen")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMeanListForIntervention(@PathParam("id") long id) {
		InterventionDAO iD = new InterventionDAO();
		iD.connect();
		List<Mean> res = iD.getById(id).getMeansList();
		iD.disconnect();
		return Response.ok(res).build();
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
	@Produces({MediaType.APPLICATION_JSON})
	public List<Intervention> getAllIntervention() {
		InterventionDAO iD = new InterventionDAO();
		iD.connect();
		List<Intervention> res = iD.getAll();
		iD.disconnect();


		if (res != null) {
            Collections.sort(res, new Comparator<Intervention>() {
                public int compare(Intervention c1, Intervention c2) {
                    long t1 = c1.getDateCreate().getTime();
                    long t2 = c2.getDateCreate().getTime();


                    return t1 > t2 ? -1 : t1 == t2 ? 0 : 1;
                }
            });
        }

		return res;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized Intervention setIntervention(Intervention intervention) {
		InterventionDAO iD = new InterventionDAO();
		iD.connect();

		RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(intervention.getAddress(), intervention.getPostcode(), intervention.getCity()); 

		Position coordinatesIntervention = adresseIntervention.getCoordinates();
		intervention.setCoordinates(coordinatesIntervention);

	//	Date now = new Date();
	//	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	//	formatter.format(now);
		intervention.setDateCreate(Datetime.getCurrentDate());

		// Génération de la liste des moyens
		intervention.generateMeanList();
		Intervention res = iD.create(intervention);
		iD.disconnect();
		
		try {
			PushServiceImpl.getInstance().sendMessage(TypeClient.ALL, "addIntervention", intervention);
		} catch (IOException e) {
			LOGGER.error("Error push service intervention", e);
		}
		
		return res;
	}
}
