package dao;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import entity.Mean;
import entity.Vehicle;
import util.Constant;
import util.Tools;

/**
 * Created by arno on 08/04/15.
 */
public class MeanDAO extends AbstractDAO<Mean>{

    /**
     * Contructeur UnityDAO
     */
    public MeanDAO()
    {
        this.datatype = Constant.DATATYPE_MEAN;
    }

    @Override
    protected Mean jsonDocumentToEntity(JsonDocument jsonDocument) {
        Mean Mean = new Mean();

        try {

            JsonObject content = jsonDocument.content();
            if (Constant.DATATYPE_MEAN.equals(((JsonObject) content.get("properties")).get("datatype"))) {
                Mean.setId(Long.parseLong(jsonDocument.id()));
                Mean.setVehicle(Vehicle.valueOf((String) ((JsonObject) content.get("properties")).get("vehicle")));
                Mean.setInPosition((Boolean) ((JsonObject) content.get("properties")).get("isInPosition"));
                Mean.setCoordinates(Tools.jsonArrayToPosition(content.getArray("coordinates")));
            } else {
                throw new IllegalArgumentException();
            }
        }
        catch(Throwable t)
        {
            Mean = null;
        }
        return Mean;
    }

    @Override
    protected JsonDocument entityToJsonDocument(Mean entity) {
        JsonObject jsonMean = entityToJsonObject(entity);
        JsonDocument doc = JsonDocument.create("" + entity.getId(), jsonMean);
        System.out.println(jsonMean);
        System.out.println(entity.getId());
        return doc;
    }

    public JsonObject entityToJsonObject(Mean entity) {
        JsonObject properties = JsonObject.create();
        properties.put("datatype", entity.getDataType());
        properties.put("isInPosition", entity.isInPosition());
        properties.put("vehicle", entity.getVehicle().toString());
        properties.put("id",entity.getId());
        JsonObject jsonMean = JsonObject.empty()
                .put("type", "Point")
                .put("coordinates", Tools.positionToJsonArray(entity.getCoordinates()))
                .put("properties", properties);
        return jsonMean;
    }
}
