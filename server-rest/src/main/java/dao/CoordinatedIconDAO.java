package dao;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

import entity.CoordinatedIcon;
import entity.Position;
import util.Constant;
import util.Tools;

/**
 * Created by jerem on 08/04/15.
 */
public class CoordinatedIconDAO extends AbstractDAO<CoordinatedIcon>  {

    @Override
    protected CoordinatedIcon jsonDocumentToEntity(JsonDocument jsonDocument) {
        CoordinatedIcon u = new CoordinatedIcon();
        try {
            JsonObject content = jsonDocument.content();
            if (Constant.DATATYPE_COORDINATED_ICON.equals(((JsonObject)content.get("properties")).get("datatype"))) {
                u.setId(Long.parseLong(jsonDocument.id()));
                u.setPosition(new Position());
            } else {
                throw new IllegalArgumentException();
            }
        } catch (Throwable t) {
            u = null;
        }
        return u;
    }

    @Override
    protected JsonDocument entityToJsonDocument(CoordinatedIcon u) {


        JsonObject propertiesIcon = JsonObject.empty()
        .put("datatype", u.getIcon().getDataType())
        .put("type", "Point");

        JsonObject jsonIcon = JsonObject.empty()
                .put("filename", u.getIcon().getFilename())
                .put("entitled", u.getIcon().getEntitled())
                .put("properties", propertiesIcon)
                .put("id", u.getIcon().getId());


        JsonObject properties = JsonObject.empty()
                .put("datatype", u.getDataType())
                .put("type", "Point");

        JsonObject jsonUser = JsonObject.empty()
                .put("icon", jsonIcon)
                .put("position", Tools.positionToJsonArray(u.getPosition()))
                .put("properties", properties)
                .put("id", u.getId());
        JsonDocument doc = JsonDocument.create("" + u.getId(), jsonUser);
        System.out.println(jsonUser);
        return doc;
    }

}
