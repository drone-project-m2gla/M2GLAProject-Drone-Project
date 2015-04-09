package rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.android.gcm.server.MulticastResult;

import service.impl.PushServiceImpl;

@Path("/register")
public class PushRegister {
	private static final Logger LOGGER = Logger.getLogger(PushRegister.class);
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void registerClient(@FormParam("id") String id) {
		PushServiceImpl.getInstance().registerClient(id);
		LOGGER.info("User connect to push service " + id);
	}
	
	@DELETE
	@Path("/{id}")
	public void unregisterClient(@PathParam("id") String id) {
		PushServiceImpl.getInstance().unregisterClient(id);
		LOGGER.info("User disconect to push service " + id);
	}
	
	@GET
	@Path("add/{scope}/{msg}")
	public MulticastResult testPush(@PathParam("scope") String scope, @PathParam("msg") String msg) {
		//FIXME Pour les tests, a supprimer
		try {
			return PushServiceImpl.getInstance().sendMessage(scope, msg);
		} catch (IOException e) {
			LOGGER.error("Error push message");
			return null;
		}
	}
}
