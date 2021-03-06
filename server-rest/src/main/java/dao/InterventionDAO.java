package dao;

import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import entity.DisasterCode;
import entity.Intervention;
import util.Constant;
import util.Tools;

/**
 * @author  alban
 * @see InterventionDAO implements these abstract methods for Intervention transformation
 */
public class InterventionDAO extends AbstractDAO<Intervention> {

    /**
     * Contructor InterventionDAO
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
            intervention.setCoordinates(Tools.arrayListToPosition((ArrayList) document.get("coordinates")));
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
        document.put("type", "Point");
        document.put("coordinates", Tools.positionToBasicDBList(entity.getCoordinates()));
        document.put("_id", entity.getId());
        return document;
    }
}
