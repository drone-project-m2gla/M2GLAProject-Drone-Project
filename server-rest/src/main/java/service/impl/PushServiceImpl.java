package service.impl;

import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import service.PushService;

/**
 * PushServiceImpl permits to use gcm
 * @author baptiste
 */
public class PushServiceImpl implements PushService {
	private static final Logger LOGGER = Logger.getLogger(PushServiceImpl.class);
	
	private static final PushService INSTANCE = new PushServiceImpl();
	private static final String SERVER_KEY = "AIzaSyBFsRgV6T1pY3Ev62Bz5KcAYTaylvgS90w";
	private static final int RETRIES = 1;
	
	private List<String> registersClient;
	private List<String> registersClientCodis;
	private List<String> registersClientSimpleuser;
	private boolean isTestMode;
	
	protected PushServiceImpl() {
		registersClient = new ArrayList<String>();
		registersClientCodis = new ArrayList<String>();
		registersClientSimpleuser = new ArrayList<String>();
		isTestMode = false;
	}
	
	public static PushService getInstance() {
		return INSTANCE;
	}
	
	public void setIsTestMode(boolean isTestMode) {
		this.isTestMode = isTestMode;
	}

	public void registerClient(TypeClient typeClient, String idClient) {
		switch (typeClient) {
			case CODIS:
				registersClientCodis.add(idClient);
				break;
			case SIMPLEUSER:
				registersClientSimpleuser.add(idClient);
				break;
			default:
				break;
		}
		registersClient.add(idClient);
	}

	public void unregisterClient(String idClient) {
		registersClient.remove(idClient);
		registersClientCodis.remove(idClient);
		registersClientSimpleuser.remove(idClient);
	}
	
	public MulticastResult sendMessage(TypeClient typeClient, String scope, Object object) throws IOException {
		if (isTestMode) {
			return null;
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		Builder messageBuilder = new Builder();
		Sender sender = new Sender(SERVER_KEY);

		mapper.writeValue(output, object);

		LOGGER.debug(output.toString());
		messageBuilder.addData(scope, output.toString());

		MulticastResult result = null;
		switch (typeClient) {
			case CODIS:
				if (registersClientCodis.isEmpty()) {
					LOGGER.warn("Not client CODIS register");
					break;
				}
				result = sender.send(messageBuilder.build(), registersClientCodis, RETRIES);
				break;
			case SIMPLEUSER:
				if (registersClientSimpleuser.isEmpty()) {
					LOGGER.warn("Not client Sapeur register");
					break;
				}
				result = sender.send(messageBuilder.build(), registersClientSimpleuser, RETRIES);
				break;
			case ALL:
				if (registersClient.isEmpty()) {
					LOGGER.warn("Not client register");
					break;
				}
				result = sender.send(messageBuilder.build(), registersClient, RETRIES);
				break;
		}
		return result;
	}
}
