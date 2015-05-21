package dao;

import com.mongodb.BasicDBList;
import org.bson.Document;
import util.Constant;
import util.Tools;import entity.GeoImage;

import java.util.ArrayList;

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
        geoImage.setCoordinates(Tools.arrayListToPosition((ArrayList) document.get("coordinates")));
        geoImage.setImageIn64(document.getString("image"));
        return geoImage;
    }

    @Override
    protected Document entityToDocument(GeoImage entity) {
        Document document = new Document();
        document.put("image", entity.getImageIn64());
        document.put("type","Point");
        document.put("coordinates", Tools.positionToBasicDBList(entity.getCoordinates()));
        document.put("_id",entity.getId());
        return document;
    }
}
