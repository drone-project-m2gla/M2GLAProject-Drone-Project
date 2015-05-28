package service.position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.GeoImage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import service.PushService.TypeClient;
import service.impl.PushServiceImpl;
import util.Configuration;
import util.Tools;
import entity.Position;

/**
 * Created by alban on 16/04/15.
 */
public class DroneThread implements Runnable, PositionUnchangedObservable {
	private static final Logger LOGGER = Logger.getLogger(DroneThread.class);
	private static DroneThread INSTANCE;

	private List<PositionUnchangedObserver> positionObservers = new ArrayList<PositionUnchangedObserver>();
	private Position position;
	private GeoImage image;
	private boolean continueThread;

	private DroneThread() {
        this.position = new Position(0.0, 0.0, 0.0);
        this.continueThread = true;
	}

	public static DroneThread getInstance() {
		return INSTANCE;
	}

	public static void createNewInstance() {
		if (INSTANCE != null) {
			INSTANCE.stopThread();
		}
		INSTANCE = new DroneThread();
	}

	public synchronized void stopThread() {
		continueThread = false;
	}

	public synchronized Position getPosition() {
		return position;
	}

	public synchronized GeoImage getImage() {
		return image;
	}

	@Override
	public void run() {
		ObjectMapper mapper = new ObjectMapper();
		HttpClient client = new HttpClient();
		while (continueThread) {
			GetMethod getPosition = new GetMethod(Configuration.getSERVER_PYTHON() + "/position");
			try {
				client.executeMethod(getPosition);
				Position position = mapper.readValue(getPosition.getResponseBodyAsString(), Position.class);
				if (position != null && !Tools.isSamePositions(this.position, position)) {
					this.position = position;
					PushServiceImpl.getInstance().sendMessage(TypeClient.SIMPLEUSER, "droneMove", position);
				} else {
					notifyObserversForPositionUnchanged();
				}
				Thread.sleep(3000);
			} catch (IOException e) {
				LOGGER.error("Get position error", e);
				return;
			} catch (InterruptedException e) {
				LOGGER.error("Get position error", e);
				return;
			}
		}
	}

	@Override
	public void addObserversPositionsUnhanged(PositionUnchangedObserver observer) {
		this.positionObservers.add(observer);
	}

	@Override
	public void removeObserversPositionsUnhanged(PositionUnchangedObserver observer) {
		this.positionObservers.remove(observer);
	}

	@Override
	public void notifyObserversForPositionUnchanged() {
		for (PositionUnchangedObserver observer : positionObservers) {
			observer.notifyPositionUnchanged();
		}
	}

	@Override
	public void flushPositionUnchangedObservers() {
		this.positionObservers.clear();
	}
}
