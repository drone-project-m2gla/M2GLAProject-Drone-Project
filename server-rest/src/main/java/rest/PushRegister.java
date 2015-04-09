package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import service.impl.PushServiceImpl;
import entity.EntityPushClient;

@Path("/register")
public class PushRegister {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void registerClient(EntityPushClient entityGCM) {
		PushServiceImpl.getInstance().registerClient(entityGCM.getId());
	}
	
	@DELETE
	@Path("/{id}")
	public void unregisterClient(@PathParam("id") String id) {
		PushServiceImpl.getInstance().unregisterClient(id);
	}
}
