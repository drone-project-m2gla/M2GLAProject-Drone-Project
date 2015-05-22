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
 * Created by arno on 12/02/15.
 */
@Path("/drone")
public class Drone {
	@GET
	@Path("position")
	@Produces(MediaType.APPLICATION_JSON)
	public Position getPosition() {
		return GetDronePositionThread.getInstance().getPosition();
	}

	@GET
	@Path("image")
	@Produces(MediaType.APPLICATION_JSON)
	public GeoImage getImage() throws IOException {
		return GetDronePositionThread.getInstance().getImage();
	}

	@POST
	@Path("target")
	@Consumes(MediaType.APPLICATION_JSON)
	public void doTrajet(Target target) {
		TransitDroneSender transitDroneSender = new TransitDroneSender(target);
		GetDronePositionThread.getInstance().flushPositionUnchangedObservers();
		GetDronePositionThread.getInstance().addObserversPositionsUnhanged(transitDroneSender);
	}

	@DELETE
	@Path("target")
	@Consumes(MediaType.APPLICATION_JSON)
	public void stopTrajet() {
		GetDronePositionThread.createNewInstance();
	}
}