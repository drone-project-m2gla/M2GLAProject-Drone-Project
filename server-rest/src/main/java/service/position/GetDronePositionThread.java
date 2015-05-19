package service.position;

import entity.Position;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import service.PushService;
import service.impl.PushServiceImpl;
import util.Configuration;
import util.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alban on 16/04/15.
 */
public class GetDronePositionThread implements Runnable, PositionUnchangedObservable {
    private List<PositionUnchangedObserver> positionObservers = new ArrayList<PositionUnchangedObserver>();
    private final Logger LOGGER = Logger.getLogger(GetDronePositionThread.class);
    private static GetDronePositionThread instance = new GetDronePositionThread();
    private Position position;
    private boolean continueThread;

    private GetDronePositionThread() {
        position = new Position(0.0, 0.0, 0.0);
        continueThread = true;
    }

    public static GetDronePositionThread getInstance()
    {
        return instance;
    }

    public static void createNewInstance()
    {
        instance.stopThread();
        instance = new GetDronePositionThread();
    }

    public synchronized void stopThread() {
        continueThread = false;
    }

    public synchronized Position getPosition() {
        return position;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        HttpClient client = new HttpClient();
        while (continueThread) {
            GetMethod get = new GetMethod(Configuration.getSERVER_PYTHON() + "/position");
            try {
                client.executeMethod(get);
                Position position = mapper.readValue(
                        get.getResponseBodyAsString(), Position.class);
                if (position != null && !Tools.isSamePositions(this.position,position)) {
                    this.position = position;
                    PushServiceImpl.getInstance().sendMessage(
                            PushService.TypeClient.SIMPLEUSER, "droneMove", position);
                }
                else
                {
                    notifyObserversForPositionUnchanged();
                }
                Thread.sleep(2987);
            } catch (IOException e) {
                //LOGGER.error("Get position error", e);
            } catch (InterruptedException e) {
                //LOGGER.error("Get position error", e);
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
        for(PositionUnchangedObserver observer : positionObservers)
        {
            observer.notifyPositionUnchanged();
        }
    }

    public void flushPositionUnchangedObservers()
    {
        this.positionObservers = new ArrayList<PositionUnchangedObserver>();
    }
}
