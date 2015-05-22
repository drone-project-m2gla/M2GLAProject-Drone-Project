package dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import util.Constant;
import entity.User;

/**
 * @author mds
 * @see UserDAO implements these abstract methods for User transformation
 */
public class UserDAO extends AbstractDAO<User> {

    /**
     * Contructor UserDAO
     */
    public UserDAO()
    {
        this.datatype = Constant.DATATYPE_USER;
    }

    public User connectUser(String username, String password) {
        BasicDBObject criterion = new BasicDBObject("username", username);
        criterion.put("password",password);
        FindIterable findIterable = collection.find(criterion);
        return documentToEntity((Document) findIterable.first());
    }

    @Override
    protected User documentToEntity(Document document) {
        if(document==null)
        {
            return null;
        }
        User u = new User();
        u.setId(document.getLong("_id"));
        u.setUsername(document.getString("username"));
        u.setPassword(document.getString("password"));
        return u;
    }

    @Override
    protected Document entityToDocument(User u) {
        Document document = new Document();
        document.put("username", u.getUsername());
        document.put("password", u.getPassword());
        document.put("_id",u.getId());
        return document;
    }

    public User getByUsername(String username) {
        FindIterable findIterable = collection.find(new BasicDBObject("username", username));
        return documentToEntity((Document) findIterable.first());
    }
}
