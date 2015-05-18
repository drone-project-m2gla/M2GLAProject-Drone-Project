package dao;

import com.mongodb.BasicDBList;
import org.bson.Document;
import util.Constant;
import util.Tools;
import entity.GeoIcon;

/**
 * Created by jerem on 08/04/15.
 */
public class GeoIconDAO extends AbstractDAO<GeoIcon>  {

    /**
     * Contructeur UnityDAO
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
        u.setPosition(Tools.documentToPosition((Document)document.get("coordinates")));
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
        document.put("coordinates", Tools.positionToDocument(u.getPosition()));
        document.put("type", "Point");
        return document;

    }
}
