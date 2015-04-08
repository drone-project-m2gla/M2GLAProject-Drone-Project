package adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.Sender;

public class AdapterGCM {
	private static final String SERVER_KEY = "AIzaSyB8e1INV7eARjM9y--ztTmYFxeJjD5VWvk";
	private static final int RETRIES = 5;
	
	private static List<String> registersClient = new ArrayList<String>();
	
	public static void addRegisterClient(String idClient) {
		registersClient.add(idClient);
	}
	
	public void sendMessage(String scope, String message) throws IOException {
		Sender sender = new Sender(SERVER_KEY);
		Builder messageBuilder = new Builder();
		messageBuilder.addData(scope, message);
		
		sender.send(messageBuilder.build(), registersClient, RETRIES);
	}
}
