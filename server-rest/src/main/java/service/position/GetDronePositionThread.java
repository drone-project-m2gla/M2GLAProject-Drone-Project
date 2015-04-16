package service.position;

import entity.Position;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import service.PushService;
import service.impl.PushServiceImpl;
import util.Configuration;

import java.io.IOException;

/**
 * Created by alban on 16/04/15.
 */
public class GetDronePositionThread implements Runnable {
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
                if (position != null && !this.position.equals(position)) {
                    this.position = position;

                    PushServiceImpl.getInstance().sendMessage(
                            PushService.TypeClient.SIMPLEUSER, "droneMove", position);
                }
                Thread.sleep(500);
            } catch (IOException e) {
                LOGGER.error("Get position error", e);
            } catch (InterruptedException e) {
                LOGGER.error("Get position error", e);
            }
        }
    }
}
