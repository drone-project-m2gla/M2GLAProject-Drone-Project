package dao;

import util.Constant;
import util.Tools;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import entity.GeoDrone;

/**
 * Created by alban on 17/03/15.
 */
public class GeoDroneDAO extends AbstractDAO<GeoDrone>{


    /**
     * Contructeur UnityDAO
     */
    public GeoDroneDAO()
    {
        this.datatype = Constant.DATATYPE_GEODRONE;
    }

    @Override
    protected GeoDrone jsonDocumentToEntity(JsonDocument jsonDocument) {
        GeoDrone geoDrone = new GeoDrone();

        try {
            JsonObject content = jsonDocument.content();
            if (Constant.DATATYPE_GEOIMAGE.equals(((JsonObject)content.get("properties")).get("datatype"))) {
                geoDrone.setId(Long.parseLong(jsonDocument.id()));
                geoDrone.setCoordinates(Tools.jsonArrayToPosition((JsonArray) content.get("coordinates")));
            } else {
                throw new IllegalArgumentException();
            }
        }
        catch(Throwable t)
        {
            geoDrone = null;
        }
        return geoDrone;
    }

    @Override
    protected JsonDocument entityToJsonDocument(GeoDrone entity) {
        JsonObject properties = JsonObject.create();
        properties.put("datatype", entity.getDataType());
        JsonObject jsonGeoImage = JsonObject.empty()
                .put("type", "Point")
                .put("coordinates", Tools.positionToJsonArray(entity.getCoordinates()))
                .put("properties", properties);
        JsonDocument doc = JsonDocument.create("" + entity.getId(), jsonGeoImage);
        return doc;
    }
}
