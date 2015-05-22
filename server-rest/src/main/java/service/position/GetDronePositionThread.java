package service.position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dao.GeoImageDAO;
import entity.GeoImage;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.map.ObjectMapper;

import util.Configuration;
import util.Tools;
import entity.GeoImage;
import entity.Position;

/**
 * Created by alban on 16/04/15.
 */
public class GetDronePositionThread implements Runnable, PositionUnchangedObservable {
	private static GetDronePositionThread instance;

	private List<PositionUnchangedObserver> positionObservers = new ArrayList<PositionUnchangedObserver>();
	private Position position;
	private GeoImage image;
	private boolean continueThread;

	private GetDronePositionThread() {
		position = new Position(0.0, 0.0, 0.0);
		continueThread = true;
	}

	public static GetDronePositionThread getInstance() {
		return instance;
	}

	public static void createNewInstance() {
		instance.stopThread();
		instance = new GetDronePositionThread();
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
					// FIXME Reactive pull service
					// PushServiceImpl.getInstance().sendMessage(PushService.TypeClient.SIMPLEUSER,
					//			"droneMove", position);

					GetMethod getImage = new GetMethod(
							Configuration.getSERVER_PYTHON() + "/picture");
					client.executeMethod(getImage);
					this.image = mapper.readValue( getImage.getResponseBodyAsString(), GeoImage.class);
                    GeoImageDAO dao = new GeoImageDAO();
                    dao.create(this.image);
				} else {
					notifyObserversForPositionUnchanged();
				}
				Thread.sleep(2987);
			} catch (IOException e) {
				// LOGGER.error("Get position error", e);
			} catch (InterruptedException e) {
				// LOGGER.error("Get position error", e);
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
		this.positionObservers = new ArrayList<PositionUnchangedObserver>();
	}
}
