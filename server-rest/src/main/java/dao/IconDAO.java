package dao;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

import entity.Icon;
import util.Constant;

/**
 * Created by jerem on 07/04/15.
 */
public class IconDAO extends AbstractDAO<Icon> {
    @Override
    protected Icon jsonDocumentToEntity(JsonDocument jsonDocument) {
        Icon u = new Icon();
        try {
            JsonObject content = jsonDocument.content();
          //  System.out.println(((JsonObject)content.get("properties")).get("datatype"));

            if (Constant.DATATYPE_ICON.equals(((JsonObject)content.get("properties")).get("datatype"))) {
                u.setId(Long.parseLong(jsonDocument.id()));
                u.setEntitled(content.getString("entitled"));
                u.setFilename(content.getString("filename"));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (Throwable t) {
            u = null;
        }
        return u;
    }

    @Override
    protected JsonDocument entityToJsonDocument(Icon u) {

        JsonObject properties = JsonObject.create();
        properties.put("datatype", u.getDataType())
        .put("type", "Point");


        JsonObject jsonUser = JsonObject.empty()
                .put("filename", u.getFilename())
                .put("entitled", u.getEntitled())
                .put("properties", properties)
                .put("id", u.getId());

        JsonDocument doc = JsonDocument.create("" + u.getId(), jsonUser);
        System.out.println(jsonUser);
        return doc;
    }
}