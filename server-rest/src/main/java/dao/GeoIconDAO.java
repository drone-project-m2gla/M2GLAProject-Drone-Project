package dao;

import org.bson.Document;

import java.util.ArrayList;

import entity.GeoIcon;
import util.Constant;
import util.Tools;

/**
 * @author :jerem
 * @see GeoIconDAO implements these abstract methods for GeoIcon transformation
 */
public class GeoIconDAO extends AbstractDAO<GeoIcon>  {

    /**
     * Contructor UnityDAO
     */
    public GeoIconDAO()
    {
        this.datatype = Constant.DATATYPE_GEOICON;
    }



    @Override
    protected GeoIcon documentToEntity(Document document) {
        if(document==null)
        {
            return null;
        }
        GeoIcon u = new GeoIcon();
        u.setId(document.getLong("_id"));
        u.setEntitled(document.getString("entitled"));
        u.setFilename(document.getString("filename"));
        u.setPosition(Tools.arrayListToPosition((ArrayList) document.get("coordinates")));
        u.setColor(document.getString("color"));
        u.setTiret(document.getBoolean("tiret"));
        u.setFirstContent(document.getString("firstContent"));
        u.setSecondContent(document.getString("secondContent"));
        return u;
    }

    @Override
    protected Document entityToDocument(GeoIcon u) {

        Document document = new Document();
        document.put("_id", u.getId());
        document.put("filename", u.getFilename());
        document.put("entitled", u.getEntitled());
        document.put("firstContent", u.getFirstContent());
        document.put("secondContent", u.getSecondContent());
        document.put("tiret", u.getTiret());
        document.put("color", u.getColor());
        document.put("coordinates", Tools.positionToBasicDBList(u.getPosition()));
        document.put("type", "Point");
        return document;

    }
}
