package dao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import org.bson.Document;
import util.Constant;
import util.Tools;
import entity.DisasterCode;
import entity.Intervention;

/**
 * Created by alban on 13/03/15.
 */
public class InterventionDAO extends AbstractDAO<Intervention> {

    /**
     * Contructeur InterventionDAO
     */
    public InterventionDAO()
    {
        this.datatype = Constant.DATATYPE_INTERVENTION;
    }

    @Override
    protected Intervention documentToEntity(Document document) {
        if(document==null)
        {
            return null;
        }
        Intervention intervention = new Intervention();
        try {
        	SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            intervention.setDateCreate(formatter.parse(document.getString("dateCreate")));
            intervention.setLabel(document.getString("label"));
            intervention.setId(document.getLong("_id"));
            intervention.setAddress(document.getString("address"));
            intervention.setPostcode(document.getString("postcode"));
            intervention.setCity(document.getString("city"));
            intervention.setDisasterCode(DisasterCode.valueOf(document.getString("disasterCode")));
            intervention.setMeansList(Tools.documentListToMeanList(((List<Document>) document.get("meansList"))));
            intervention.setMeansXtra(Tools.documentListToMeanList(((List<Document>) document.get("meansXtra"))));
            intervention.setCoordinates(Tools.documentToPosition((Document) document.get("coordinates")));
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            intervention = null;
        }
        return intervention;
    }

    @Override
    protected Document entityToDocument(Intervention entity) {
        if(entity==null)
        {
            return null;
        }
        Document document = new Document();
        document.put("dateCreate", entity.getDateCreate().toString());
        document.put("label", entity.getLabel());
        document.put("address", entity.getAddress());
        document.put("city", entity.getCity());
        document.put("postcode", entity.getPostcode());
        document.put("disasterCode", entity.getDisasterCode().toString());
        document.put("meansList",Tools.meanListToBasicDBList(entity.getMeansList()));
        document.put("meansXtra",Tools.meanListToBasicDBList(entity.getMeansXtra()));
        document.put("type", "Point");
        document.put("coordinates", Tools.positionToDocument(entity.getCoordinates()));
        document.put("_id", entity.getId());
        return document;
    }
}
