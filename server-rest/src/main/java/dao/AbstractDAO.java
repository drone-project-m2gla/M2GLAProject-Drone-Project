package dao;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entity.AbstractEntity;
import util.Configuration;

/**
 * @see
 * AbstractDAO provides methods for DAO
 * Also use for connect and disconnect
 * This class is used by other DAO classes for the generic methods
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
     * Connect to BDD and bucket to communicate with couchbase
     */
    public final void connect() {
        if(mongoClient == null) {
            MongoCredential credential = MongoCredential.createCredential(Configuration.getMONGODB_USER(), Configuration.getDATABASE_NAME(), Configuration.getMONGODB_PWD().toCharArray());
            mongoClient = new MongoClient( new ServerAddress(Configuration.getMONGODB_HOSTNAME(), Integer.parseInt(Configuration.getMONGODB_PORT())), Arrays.asList(credential));
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
            mongoClient = null;
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
     * @param e entity to delete
     */
    public final void delete(T e) {
        collection.deleteOne(new BasicDBObject("_id", e.getId()));
    }

    /**
     * Update entity
     * @param e entity to update
     */
    public final T update(T e) {
        collection.replaceOne(new BasicDBObject("_id", e.getId()), entityToDocument(e));
        return this.getById(e.getId());
    }

    /**
     * GetAll
     * @return List<T>
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
     * @return T
     */
    public final T getById(long id)
    {
        FindIterable findIterable = collection.find(new BasicDBObject("_id", id));
        return documentToEntity((Document) findIterable.first());
    }

    /**
     * Transform a jsonDocument to entity
     * Abstract method implemented on multiple inheritance
     * @param document document to transform
     * @return entity of Document
     */
    protected abstract T documentToEntity(Document document);

    /**
     * Transform an entity to Document
     * Abstract method implemented on multiple inheritance
     * @param entity to transform
     * @return document of entity
     */
    protected abstract Document entityToDocument(T entity);

    /**
     * Create different indexes
     */
    public void ensureIndex()
    {

    }
}
