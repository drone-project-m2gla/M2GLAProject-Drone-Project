package dao;

import com.mongodb.BasicDBList;
import org.bson.Document;
import util.Constant;
import util.Tools;import entity.GeoImage;

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
        GeoImage geoImage = new GeoImage();
        geoImage.setId(document.getLong("_id"));
        geoImage.setCoordinates(Tools.documentToPosition((Document) document.get("coordinates")));
        geoImage.setImageIn64(document.getString("image"));
        return geoImage;
    }

    @Override
    protected Document entityToDocument(GeoImage entity) {
        Document document = new Document();
        document.put("image", entity.getImageIn64());
        document.put("type","Point");
        document.put("coordinates", Tools.positionToDocument(entity.getCoordinates()));
        document.put("_id",entity.getId());
        return document;
    }
}
