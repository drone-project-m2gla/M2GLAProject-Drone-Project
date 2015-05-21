package rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import service.position.GetDronePositionThread;
import service.position.TransitDroneSender;
import entity.ImageDrone;
import entity.Position;
import entity.Target;

/**
 * Created by arno on 12/02/15.
 */
@Path("/drone")
public class Drone {
	@GET
	@Path("move")
	@Produces(MediaType.APPLICATION_JSON)
	public Position getPosition() {
		return GetDronePositionThread.getInstance().getPosition();
	}

	@GET
	@Path("image")
	@Produces(MediaType.APPLICATION_JSON)
	public ImageDrone getImage() throws IOException {
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
}