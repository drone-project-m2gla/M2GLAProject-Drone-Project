package service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import service.PushService;

import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class PushServiceImpl implements PushService {
	private static final PushService INSTANCE = new PushServiceImpl();
	private static final String SERVER_KEY = "AIzaSyB8e1INV7eARjM9y--ztTmYFxeJjD5VWvk";
	private static final int RETRIES = 5;
	
	private List<String> registersClient;
	
	protected PushServiceImpl() {
		registersClient = new ArrayList<String>();
	}
	
	public static PushService getInstance() {
		return INSTANCE;
	}

	public void registerClient(String idClient) {
		registersClient.add(idClient);
	}

	public void unregisterClient(String idClient) {
		registersClient.remove(idClient);
	}
	
	public MulticastResult sendMessage(String scope, String message) throws IOException {
		Sender sender = new Sender(SERVER_KEY);
		Builder messageBuilder = new Builder();
		messageBuilder.addData(scope, message);
		
		return sender.send(messageBuilder.build(), registersClient, RETRIES);
	}
}
