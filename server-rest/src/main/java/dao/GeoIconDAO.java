package dao;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

import entity.GeoIcon;
import util.Constant;
import util.Tools;

/**
 * Created by jerem on 08/04/15.
 */
public class GeoIconDAO extends AbstractDAO<GeoIcon>  {

    /**
     * Contructeur UnityDAO
     */
    public GeoIconDAO()
    {
        this.datatype = Constant.DATATYPE_GEOICON;
    }



    @Override
    protected GeoIcon jsonDocumentToEntity(JsonDocument jsonDocument) {
        GeoIcon u = new GeoIcon();
        try {
            JsonObject content = jsonDocument.content();
            if (Constant.DATATYPE_GEOICON.equals(((JsonObject)content.get("properties")).get("datatype"))) {
                u.setId(Long.parseLong(jsonDocument.id()));
                u.setEntitled(((JsonObject) content.get("properties")).getString("entitled"));
                u.setFilename(((JsonObject) content.get("properties")).getString("filename"));
                u.setPosition(  Tools.jsonArrayToPosition(content.getArray("coordinates")));
                u.setColor(((JsonObject) content.get("properties")).getString("color"));
                u.setTiret((Boolean) ((JsonObject) content.get("properties")).getBoolean("tiret"));
                u.setFirstContent(((JsonObject) content.get("properties")).getString("firstContent"));
                u.setSecondContent(((JsonObject) content.get("properties")).getString("secondContent"));

            } else {
                throw new IllegalArgumentException();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            u = null;
        }
        return u;
    }

    @Override
    protected JsonDocument entityToJsonDocument(GeoIcon u) {

        JsonObject properties = JsonObject.empty()
                .put("datatype", u.getDataType())
                .put("id", u.getId())
                .put("filename", u.getFilename())
                .put("entitled", u.getEntitled())
                .put("firstContent", u.getFirstContent())
                .put("secondContent", u.getSecondContent())
                .put("tiret", u.getTiret())
                .put("color", u.getColor())

                ;

        JsonObject jsonUser = JsonObject.empty()
                .put("coordinates", Tools.positionToJsonArray(u.getPosition()))
                .put("properties", properties)
                .put("type", "Point");
        JsonDocument doc = JsonDocument.create("" + u.getId(), jsonUser);
        return doc;

    }
}
