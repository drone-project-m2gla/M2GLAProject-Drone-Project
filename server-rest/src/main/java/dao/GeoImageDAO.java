package dao;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import entity.GeoImage;
import util.Constant;
import util.Datetime;
import util.Tools;

/**
 * @author alban
 * @see GeoImageDAO implements these abstract methods for GeoImage transformation
 */
public class GeoImageDAO extends AbstractDAO<GeoImage> {

    /**
     * Contructor UnityDAO
     */
    public GeoImageDAO()
    {
        this.datatype = Constant.DATATYPE_GEOIMAGE;
    }

    @Override
    protected GeoImage documentToEntity(Document document) {
        if(document==null)
        {
            return null;
        }
        GeoImage geoImage = new GeoImage();
        geoImage.setId(document.getLong("_id"));
        geoImage.setPosition(Tools.arrayListToPosition((ArrayList) document.get("position")));
        geoImage.setImage(document.getString("image"));
        geoImage.setWidth(document.getInteger("width"));
        geoImage.setHeight(document.getInteger("height"));
        if(document.getDate("date") != null)
        {
            geoImage.setDate(document.getDate("date"));
        }
        if(document.getLong("interventionId") != null)
        {
            geoImage.setInterventionId(document.getLong("interventionId"));
        }

        return geoImage;
    }

    @Override
    protected Document entityToDocument(GeoImage entity) {
        Document document = new Document();
        document.put("image", entity.getImage());
        document.put("height", entity.getHeight());
        document.put("width",entity.getWidth());
        document.put("position", Tools.positionToBasicDBList(entity.getPosition()));
        document.put("interventionId", entity.getInterventionId());
        document.put("date", Datetime.getCurrentDate());
        document.put("_id",entity.getId());
        return document;
    }

    public List<GeoImage> getAllImagesNear(float latitude, float longitude, int limit)
    {
        final List<GeoImage> res = new ArrayList<GeoImage>();
        BasicDBObject geometry = new BasicDBObject("type", "Point");
        float[] coordinates =  {latitude,longitude };
        geometry.put("coordinates", coordinates);
        BasicDBObject near = new BasicDBObject("$geometry", geometry);
        near.put("$maxDistance",Constant.NEAR_REQUEST_MAXDISTANCE);
        BasicDBObject position = new BasicDBObject("$near", near);
        BasicDBObject criteria = new BasicDBObject("position", position);
        FindIterable findIterable = collection.find(criteria).limit(limit);
        findIterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                res.add(documentToEntity(document));
            }
        });
        return res;
    }

    @Override
    public void ensureIndex()
    {
        BasicDBObject position = new BasicDBObject("position", "2dsphere");
        collection.createIndex(position);
    }
}
