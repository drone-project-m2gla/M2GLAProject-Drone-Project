package service;

import java.io.IOException;

public interface PushService {
	public void registerClient(String idClient);
	public void unregisterClient(String idClient);
	public void sendMessage(String scope, String message) throws IOException;
}
