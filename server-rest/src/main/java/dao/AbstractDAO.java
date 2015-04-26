package dao;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import util.Configuration;
import entity.AbstractEntity;

/**
 * Abstract class for DAO
 * AbstractDAO provides methods for DAO
 * Also use for connect and disconnect
 */
public abstract class AbstractDAO<T extends AbstractEntity> {

    private MongoClient mongoClient;
    private MongoDatabase db;
    protected MongoCollection collection;

    /**
     * datatype of T
     */
    protected String datatype;

    /**
     * Connect to BDD and
     * @return Bucket to communicate with couchbase
     */
    public final void connect() {
        if(mongoClient == null || db==null) {
            mongoClient = new MongoClient(Configuration.getMONGODB_HOSTNAME(), Integer.parseInt(Configuration.getMONGODB_PORT()));
            db = mongoClient.getDatabase(Configuration.getDATABASE_NAME());
            collection = db.getCollection(datatype);
        }
    }

    /**
     * Disconnect BDD
     */
    public final void disconnect() {
        if(mongoClient != null)
        {
            mongoClient.close();
            db =null;
            collection = null;
        }
    }

    /**
     * Create an entity
     * @param e entity to create
     */
    public final T create(T e) {
        collection.insertOne(entityToDocument(e));
        return this.getById(e.getId());
    }

    /**
     * Delete an entity
     * @param e
     */
    public final void delete(T e) {
        collection.deleteOne(new BasicDBObject("_id",e.getId()));
    }

    /**
     * Update entity
     * @param e
     */
    public final T update(T e) {
        collection.replaceOne(new BasicDBObject("_id", e.getId()), entityToDocument(e));
        return this.getById(e.getId());
    }

    /**
     * GetAll
     * @return
     */
    public final List<T> getAll()
    {
        final List<T> res = new ArrayList<T>();
        FindIterable findIterable = collection.find();
        findIterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                res.add(documentToEntity(document));
            }
        });
        return res;
    }

    /**
     * GetById
     * @return
     */
    public final T getById(long id)
    {
        FindIterable findIterable = collection.find(new BasicDBObject("_id", id));
        return documentToEntity((Document) findIterable.first());
    }

    /**
     * flush our bucket
     * @return
     */
    public boolean flush()
    {
        db.drop();
        return true;
    }

    /**
     * Transform a jsonDocument to entity
     * @param document document to transform
     * @return entity of Document
     */
    protected abstract T documentToEntity(Document document);

    /**
     * Transform an entity to Document
     * @param entity to transform
     * @return document of entity
     */
    protected abstract Document entityToDocument(T entity);
}
