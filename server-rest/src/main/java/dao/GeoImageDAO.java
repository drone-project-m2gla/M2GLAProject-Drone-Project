package dao;

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
}
