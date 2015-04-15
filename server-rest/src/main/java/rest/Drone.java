package rest;

import entity.Position;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import service.PushService.TypeClient;
import service.impl.PushServiceImpl;
import util.Configuration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by arno on 12/02/15.
 */
@Path("/drone")
public class Drone {
	private static final Logger LOGGER = Logger.getLogger(Drone.class);

	private GetPositionThread positionThread;

	@POST
	@Path("move")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setPosition(Position position) {
		HttpClient client = new HttpClient();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		PostMethod postMethod = new PostMethod(Configuration.SERVER_PYTHON + "/position");

		try {
			mapper.writeValue(output, position);

			RequestEntity requestEntity = new StringRequestEntity(
					output.toString(), MediaType.APPLICATION_JSON, "UTF-8");
			postMethod.setRequestEntity(requestEntity);

			client.executeMethod(postMethod);
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}

		LOGGER.info("Code de retour setPosition drone :" + postMethod.getStatusCode());

		return Response.status(postMethod.getStatusCode()).build();
	}

	@GET
	@Path("startListenerPosition")
	public void startListenerPosition() {
		positionThread = new GetPositionThread();
		new Thread(positionThread).start();
	}

	@GET
	@Path("endListenerPosition")
	public void endListenerPosition() {
		positionThread.endMove();
	}

	@GET
	@Path("move")
	public Position getPosition() {
		return positionThread.getPosition();
	}
	
	private class GetPositionThread implements Runnable {
		private final Logger LOGGER = Logger.getLogger(GetPositionThread.class);

		public Position position;
		public boolean move;

		public GetPositionThread() {
			position = new Position(0.0, 0.0, 0.0);
			move = true;
		}
		
		public synchronized void endMove() {
			move = false;
		}
		
		public synchronized Position getPosition() {
			return position;
		}

		@Override
		public void run() {
			ObjectMapper mapper = new ObjectMapper();
			HttpClient client = new HttpClient();
			while (move) {
				GetMethod get = new GetMethod(Configuration.SERVER_PYTHON + "/position");
				try {
					client.executeMethod(get);

					Position position = mapper.readValue(
							get.getResponseBodyAsString(), Position.class);

					if (position != null && !this.position.equals(position)) {
						this.position = position;

						PushServiceImpl.getInstance().sendMessage(
								TypeClient.SIMPLEUSER, "droneMove", position);
					}
				} catch (IOException e) {
					LOGGER.error("Get position error", e);
				}
			}
		}
	}

}