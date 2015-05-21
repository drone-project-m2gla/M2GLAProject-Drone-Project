package rest;

import entity.ImageDrone;
import entity.Position;
import entity.Target;
import groovyjarjarantlr.ASdebug.IASDebugStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import service.PushService.TypeClient;
import service.impl.PushServiceImpl;
import service.position.GetDronePositionThread;
import service.position.TransitDroneSender;
import util.Configuration;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by arno on 12/02/15.
 */
@Path("/drone")
public class Drone {
	private static final Logger LOGGER = Logger.getLogger(Drone.class);

	/**
	 * Just for test
	 */
	@POST
	@Path("move")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setPosition(Position position) {
		HttpClient client = new HttpClient();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		PostMethod postMethod = new PostMethod(Configuration.getSERVER_PYTHON() + "/position");

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

	/**
	 * Just for test
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@GET
	@Path("testImage")
	@Produces("image/jpeg")
	public byte[] getTestImage() throws HttpException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		HttpClient client = new HttpClient();
		GetMethod getImage = new GetMethod(Configuration.getSERVER_PYTHON() + "/picture");
		client.executeMethod(getImage);
		ImageDrone image = mapper.readValue(getImage.getResponseBodyAsString(), ImageDrone.class);
		return image.getImage();
	}

	@GET
	@Path("move")
	@Produces(MediaType.APPLICATION_JSON)
	public Position getPosition() {
		return GetDronePositionThread.getInstance().getPosition();
	}

	@GET
	@Path("image")
	@Produces("image/jpeg")
	public byte[] getImage() throws IOException {
		ImageDrone image = GetDronePositionThread.getInstance().getImage();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(image.getImage()));
		BufferedImage outputImage = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_RGB);
		outputImage.createGraphics().drawImage(inputImage, 0, 0, Color.WHITE, null);
		ImageIO.write(outputImage, "jpg", output);
		return output.toByteArray();
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