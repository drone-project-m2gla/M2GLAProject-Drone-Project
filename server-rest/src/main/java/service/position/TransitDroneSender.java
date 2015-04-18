package service.position;

import entity.Position;
import entity.Target;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import util.Configuration;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by alban on 16/04/15.
 */
public class TransitDroneSender implements PositionUnchangedObserver {
    private static final Logger LOGGER = Logger.getLogger(TransitDroneSender.class);
    private Target target;
    private int nextIndex = 0;
    private boolean isIncrement;

    public TransitDroneSender(Target target) {
        this.target = target;
        this.isIncrement = true;
    }

    @Override
    public void notifyPositionUnchanged() {
        if(target.getPositions().size()>0) {
            HttpClient client = new HttpClient();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            PostMethod postMethod = new PostMethod(Configuration.getSERVER_PYTHON() + "/position");

            try {
                mapper.writeValue(output, target.getPositions().get(nextIndex));

                RequestEntity requestEntity = new StringRequestEntity(
                        output.toString(), MediaType.APPLICATION_JSON, "UTF-8");
                postMethod.setRequestEntity(requestEntity);

                client.executeMethod(postMethod);
            } catch (IOException e) {
                LOGGER.error("Error target", e);
            }
            if (target.getPositions().size() > 1) {
                if (target.isClose() && nextIndex >= target.getPositions().size()) {
                    nextIndex = 0;
                } else if (!target.isClose() && ((isIncrement && nextIndex >= target.getPositions().size() - 1) || (!isIncrement && nextIndex <= 0))) {
                    isIncrement = !isIncrement;
                }

                nextIndex = nextIndex + ((isIncrement) ? 1 : -1);
            }
        }
    }
}
