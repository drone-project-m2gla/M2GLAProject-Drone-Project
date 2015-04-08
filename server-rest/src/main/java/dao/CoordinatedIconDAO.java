package dao;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

import entity.CoordinatedIcon;
import entity.Position;
import util.Constant;

/**
 * Created by jerem on 08/04/15.
 */
public class CoordinatedIconDAO extends AbstractDAO<CoordinatedIcon>  {

    @Override
    protected CoordinatedIcon jsonDocumentToEntity(JsonDocument jsonDocument) {
        CoordinatedIcon u = new CoordinatedIcon();
        try {
            JsonObject content = jsonDocument.content();
            if (Constant.DATATYPE_COORDINATED_ICON.equals(content.getString("datatype"))) {
                u.setId(Long.parseLong(jsonDocument.id()));
                u.setPosition(new Position());
                u.setId(7879);
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

        JsonObject properties = JsonObject.create();
        properties.put("datatype", u.getDataType())
                .put("type", "Point");


        JsonObject jsonUser = JsonObject.empty()
                .put("icon", u.getIcon())
                .put("position", u.getPosition())
                .put("properties", properties)
                .put("id", u.getId());
        JsonDocument doc = JsonDocument.create("" + u.getId(), jsonUser);
        System.out.println(jsonUser);
        return doc;
    }

}
