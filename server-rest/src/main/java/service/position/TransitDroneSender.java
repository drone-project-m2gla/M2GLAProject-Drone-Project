package service.position;

import dao.GeoImageDAO;
import entity.GeoImage;
import entity.Target;

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

import javax.ws.rs.core.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by alban on 16/04/15.
 */
public class TransitDroneSender implements PositionUnchangedObserver {
	private static final Logger LOGGER = Logger
			.getLogger(TransitDroneSender.class);
	private Target target;
	private int index = 0;
	private boolean isIncrement;

	public TransitDroneSender(Target target) {
		this.target = target;
		this.isIncrement = true;
	}

	@Override
	public void notifyPositionUnchanged() {
		try {
			HttpClient client = new HttpClient();
			ObjectMapper mapper = new ObjectMapper();

			GetMethod getImage = new GetMethod(Configuration.getSERVER_PYTHON() + "/picture");
			client.executeMethod(getImage);

			GeoImage image = mapper.readValue(
					getImage.getResponseBodyAsString(), GeoImage.class);
			image.setInterventionId(target.getInterventionId());

			GeoImageDAO dao = new GeoImageDAO();
			dao.connect();
			dao.create(image);
			dao.disconnect();

			PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "imageDrone", image);

			if (target.getPositions().size() > 0) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				PostMethod postMethod = new PostMethod( Configuration.getSERVER_PYTHON() + "/position");
				mapper.writeValue(output, target.getPositions().get(index));
				RequestEntity requestEntity = new StringRequestEntity( output.toString(), MediaType.APPLICATION_JSON, "UTF-8");
				postMethod.setRequestEntity(requestEntity);
				client.executeMethod(postMethod);
				LOGGER.trace("send position " + index + " : " + target.getPositions().get(index));
				if (target.getPositions().size() > 1) {
					if (target.isClose()) {
						index = (index >= target.getPositions().size() - 1) ? 0 : index + 1;
					} else {
						if (isIncrement) {
							if (index >= target.getPositions().size() - 1) {
								isIncrement = false;
							}
						} else {
							if (index == 0) {
								isIncrement = true;
							}
						}
						index = index + ((isIncrement) ? 1 : -1);
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error target or image", e);
		}
	}
}
