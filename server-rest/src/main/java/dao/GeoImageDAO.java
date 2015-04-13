package dao;

import util.Constant;
import util.Tools;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import entity.GeoImage;

/**
 * Created by alban on 16/03/15.
 */
public class GeoImageDAO extends AbstractDAO<GeoImage> {

    /**
     * Contructeur UnityDAO
     */
    public GeoImageDAO()
    {
        this.datatype = Constant.DATATYPE_GEOIMAGE;
    }

    @Override
    protected GeoImage jsonDocumentToEntity(JsonDocument jsonDocument) {
        GeoImage geoImage = new GeoImage();

        try {
            JsonObject content = jsonDocument.content();
            if (Constant.DATATYPE_GEOIMAGE.equals(((JsonObject)content.get("properties")).get("datatype"))) {
                geoImage.setId(Long.parseLong(jsonDocument.id()));
                geoImage.setCoordinates(Tools.jsonArrayToPosition((JsonArray) content.get("coordinates")));
                geoImage.setImageIn64((String)((JsonObject)content.get("properties")).get("image"));
            } else {
                throw new IllegalArgumentException();
            }
        }
        catch(Throwable t)
        {
            geoImage = null;
        }
        return geoImage;
    }

    @Override
    protected JsonDocument entityToJsonDocument(GeoImage entity) {
        JsonObject properties = JsonObject.create();
        properties.put("datatype", entity.getDataType());
        properties.put("image", entity.getImageIn64());
        JsonObject jsonGeoImage = JsonObject.empty()
                .put("type","Point")
                .put("coordinates", Tools.positionToJsonArray(entity.getCoordinates()))
                .put("properties", properties);
        JsonDocument doc = JsonDocument.create("" + entity.getId(), jsonGeoImage);
        return doc;
    }
}
