package service;

import java.io.IOException;

import com.google.android.gcm.server.MulticastResult;

public interface PushService {
	public enum TypeClient {
		CODIS, SIMPLEUSER, ALL
	}
	public void setIsTestMode(boolean isTestMode);
	public void registerClient(TypeClient typeClient, String idClient);
	public void unregisterClient(String idClient);
	public MulticastResult sendMessage(TypeClient typeClient, String scope, Object object) throws IOException;
}
