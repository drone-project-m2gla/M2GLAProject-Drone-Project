package service;

import java.io.IOException;

import com.google.android.gcm.server.MulticastResult;

public interface PushService {
	public void registerClient(String idClient);
	public void unregisterClient(String idClient);
	public MulticastResult sendMessage(String scope, String message) throws IOException;
}
