package dao;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import entity.User;
import util.Constant;

/**
 * Created by mds on 07/04/15.
 * Class ${CLASS}
 */
public class UserDAO extends AbstractDAO<User> {

    public JsonDocument findByLogin(String username, String password) {
        JsonDocument res = this.currentBucket.get(Constant.DATATYPE_USER);
        res.content().get("username");
        return res;
    }

    @Override
    protected User jsonDocumentToEntity(JsonDocument jsonDocument) {
        User u = new User();
        try {
            JsonObject content = jsonDocument.content();
            if (Constant.DATATYPE_UNITY.equals(content.getString("datatype"))) {
                u.setId(Long.parseLong(jsonDocument.id()));
                u.setUsername(content.getString("username"));
                u.setPassword(content.getString("password"));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (Throwable t) {
            u = null;
        }
        return u;
    }

    @Override
    protected JsonDocument entityToJsonDocument(User u) {
        JsonObject properties = JsonObject.create();
        properties.put("datatype", u.getDataType())
                .put("username", u.getUsername())
                .put("password", u.getPassword());

        JsonObject jsonUser = JsonObject.empty()
                .put("properties", properties);

        JsonDocument doc = JsonDocument.create("" + u.getUsername(), jsonUser);
        System.out.println(jsonUser);
        return doc;
    }
}
