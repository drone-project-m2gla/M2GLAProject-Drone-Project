package rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entity.GeoImage;
import service.position.GetDronePositionThread;
import service.position.TransitDroneSender;
import entity.Position;
import entity.Target;

/**
 * @author arno on 12/02/15
 */
@Path("/drone")
public class Drone {
    /**
     * @return the position of the drone
     */
	@GET
	@Path("position")
	@Produces(MediaType.APPLICATION_JSON)
	public Position getPosition() {
		return GetDronePositionThread.getInstance().getPosition();
	}

	/**
     * @return the picture of the drone
     */
	@GET
	@Path("image")
	@Produces(MediaType.APPLICATION_JSON)
	public GeoImage getImage() throws IOException {
		return GetDronePositionThread.getInstance().getImage();
	}

    /**
     * add the ride of the drone
     */
	@POST
	@Path("target")
	@Consumes(MediaType.APPLICATION_JSON)
	public void doTrajet(Target target) {
		TransitDroneSender transitDroneSender = new TransitDroneSender(target);
		GetDronePositionThread.createNewInstance();
		GetDronePositionThread.getInstance().addObserversPositionsUnhanged(transitDroneSender);
	}

	/**
     * delete the ride of the drone
     */
	@DELETE
	@Path("target")
	@Consumes(MediaType.APPLICATION_JSON)
	public void stopTrajet() {
		GetDronePositionThread.getInstance().stopThread();
		GetDronePositionThread.getInstance().flushPositionUnchangedObservers();
	}
}