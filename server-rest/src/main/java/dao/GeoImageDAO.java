package dao;

import org.bson.Document;

import java.util.ArrayList;

import entity.GeoImage;
import util.Constant;
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
