package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("register")
public class PushRegister {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void registerClient() {
		
	}
}
