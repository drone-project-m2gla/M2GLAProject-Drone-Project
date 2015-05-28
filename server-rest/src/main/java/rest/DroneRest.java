package rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dao.TargetDAO;
import entity.GeoImage;
import service.position.DroneThread;
import service.position.TransitDroneSender;
import entity.Position;
import entity.Target;

/**
 * @author arno on 12/02/15
 */
@Path("/drone")
public class DroneRest {
	private Target target;

    /**
     * @return the position of the drone
     */
	@GET
	@Path("position")
	@Produces(MediaType.APPLICATION_JSON)
	public Position getPosition() {
		return DroneThread.getInstance().getPosition();
	}

	/**
     * @return the picture of the drone
     */
	@GET
	@Path("image")
	@Produces(MediaType.APPLICATION_JSON)
	public GeoImage getImage() throws IOException {
		return DroneThread.getInstance().getImage();
	}

    /**
     * get the ride of the drone
     */
	@GET
	@Path("target/{interventionId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Target getTrajet(@PathParam("interventionId") int interventionId) {
		return target;
	}

    /**
     * add the ride of the drone
     */
	@POST
	@Path("target")
	@Consumes(MediaType.APPLICATION_JSON)
	public void doTrajet(Target target) {
		TargetDAO targetDAO = new TargetDAO();
		targetDAO.connect();
		target = targetDAO.create(target);
		targetDAO.disconnect();
		
		TransitDroneSender transitDroneSender = new TransitDroneSender(target);

		DroneThread.createNewInstance();
		DroneThread.getInstance().flushPositionUnchangedObservers();
		DroneThread.getInstance().addObserversPositionsUnhanged(transitDroneSender);

		new Thread(DroneThread.getInstance()).start();
	}

	/**
     * delete the ride of the drone
     */
	@DELETE
	@Path("target")
	public void stopTrajet() {
        if (DroneThread.getInstance() != null) {
        	DroneThread.getInstance().flushPositionUnchangedObservers();
        	DroneThread.getInstance().stopThread();
        }
	}
}