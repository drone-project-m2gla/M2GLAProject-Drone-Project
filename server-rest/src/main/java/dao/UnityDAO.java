package dao;

import util.Constant;
import util.Tools;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import entity.Unity;

/**
 * Created by alban on 12/03/15.
 * DAO pour @see Unity
 */
public class UnityDAO extends AbstractDAO<Unity> {

    /**
     * Contructeur UnityDAO
     */
    public UnityDAO()
    {
        this.datatype = Constant.DATATYPE_UNITY;
    }

    /**
     * Transform un jsondoc en Unity
     * @param jsonDocument document to transform
     * @return
     */
    @Override
    protected Unity jsonDocumentToEntity(JsonDocument jsonDocument) {
        Unity u = new Unity();
        try {
            JsonObject content = jsonDocument.content();
            if (Constant.DATATYPE_UNITY.equals(((JsonObject)content.get("properties")).get("datatype"))) {
                u.setId(Long.parseLong(jsonDocument.id()));
                u.setName((String)content.get("name"));
                u.setUnitPosition(Tools.jsonArrayToPosition((JsonArray)content.get("position")));
            } else {
                throw new IllegalArgumentException();
            }
        }
        catch(Throwable t)
        {
            u = null;
        }
        return u;
    }

    /**
     * Tr
     * @param u
     * @return
     */
    @Override
    protected JsonDocument entityToJsonDocument(Unity u) {

        JsonObject properties = JsonObject.create();
        properties.put("datatype", u.getDataType());

        JsonObject jsonUser = JsonObject.empty()
                .put("position", Tools.positionToJsonArray(u.getUnitPosition()))
                .put("name", u.getName())
                .put("properties", properties);
        JsonDocument doc = JsonDocument.create(""+u.getId(), jsonUser);
        System.out.println(jsonUser);
        return doc;
    }

}
