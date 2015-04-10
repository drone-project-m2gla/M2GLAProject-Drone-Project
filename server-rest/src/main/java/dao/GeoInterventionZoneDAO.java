package dao;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import entity.GeoInterventionZone;
import util.Constant;
import util.Tools;

/**
 * Created by alban on 13/03/15.
 */
public class GeoInterventionZoneDAO extends AbstractDAO<GeoInterventionZone>{

    /**
     * Contructeur UnityDAO
     */
    public GeoInterventionZoneDAO()
    {
        this.datatype = Constant.DATATYPE_GEOINTERVENTIONZONE;
    }

    @Override
    protected GeoInterventionZone jsonDocumentToEntity(JsonDocument jsonDocument) {
        GeoInterventionZone geo = new GeoInterventionZone();

        try {
            JsonObject content = jsonDocument.content();
            if (Constant.DATATYPE_GEOINTERVENTIONZONE.equals(((JsonObject) content.get("properties")).get("datatype"))) {
                geo.setId(Long.parseLong(jsonDocument.id()));
                geo.setCoordinates(Tools.jsonArrayToZoneList(content.getArray("coordinates")));
            } else {
                throw new IllegalArgumentException();
            }
        }
        catch(Throwable t)
        {
            geo = null;
        }
        return geo;
    }

    @Override
    protected JsonDocument entityToJsonDocument(GeoInterventionZone entity) {
        JsonObject properties = JsonObject.create();
        properties.put("datatype", entity.getDataType());
        JsonObject jsonGeoInterventionZone = JsonObject.empty()
                .put("type","Polygon")
                .put("coordinates", Tools.zoneListToJsonArray(entity.getCoordinates()))
                .put("properties", properties);
        JsonDocument doc = JsonDocument.create("" + entity.getId(), jsonGeoInterventionZone);
        return doc;
    }
}
