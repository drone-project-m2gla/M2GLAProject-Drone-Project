package dao;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import util.Constant;
import util.Tools;import entity.GeoImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alban on 16/03/15.
 */
public class GeoImageDAO extends AbstractDAO<GeoImage> {

    /**
     * Contructeur UnityDAO
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
        return geoImage;
    }

    @Override
    protected Document entityToDocument(GeoImage entity) {
        Document document = new Document();
        document.put("image", entity.getImage());
        document.put("height", entity.getHeight());
        document.put("width",entity.getWidth());
        document.put("position", Tools.positionToBasicDBList(entity.getPosition()));
        document.put("_id",entity.getId());
        return document;
    }

    public List<GeoImage> getAllImagesNear(float latitude, float longitude)
    {
        final List<GeoImage> res = new ArrayList<GeoImage>();
        BasicDBObject geometry = new BasicDBObject("type", "Point");
        float[] coordinates =  {latitude,longitude };
        geometry.put("coordinates", coordinates);
        BasicDBObject near = new BasicDBObject("$geometry", geometry);
        near.put("$maxDistance",Constant.NEAR_REQUEST_MAXDISTANCE);
        BasicDBObject position = new BasicDBObject("$near", near);
        BasicDBObject criteria = new BasicDBObject("position", position);
        FindIterable findIterable = collection.find(criteria);
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
