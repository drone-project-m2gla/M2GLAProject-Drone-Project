package dao;

import util.Constant;
import util.Tools;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import entity.DisasterCode;
import entity.Intervention;
import entity.Mean;

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
    protected Intervention jsonDocumentToEntity(JsonDocument jsonDocument) {
        Intervention intervention = new Intervention();
        try {
            JsonObject content = jsonDocument.content();
            if (Constant.DATATYPE_INTERVENTION.equals(((JsonObject) content.get("properties")).get("datatype"))) {
                intervention.setId(Long.parseLong(jsonDocument.id()));
                intervention.setAddress((String) ((JsonObject)content.get("properties")).get("address"));
                intervention.setPostcode((String) ((JsonObject)content.get("properties")).get("postcode"));
                intervention.setCity((String) ((JsonObject)content.get("properties")).get("city"));
                intervention.setDisasterCode(DisasterCode.valueOf((String) ((JsonObject)content.get("properties")).get("disasterCode")));
                intervention.setMeansList(Tools.jsonArrayToMeanList((JsonArray) ((JsonObject)content.get("properties")).get("meansList")));
                if ((JsonArray) ((JsonObject)content.get("properties")).get("meansXtra") != null) {
                    intervention.setMeansXtra(Tools.jsonArrayToMeanList((JsonArray) ((JsonObject) content.get("properties")).get("meansXtra")));
                }
                intervention.setCoordinates(Tools.jsonArrayToPosition(content.getArray("coordinates")));

            } else {
                throw new IllegalArgumentException();
            }
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            intervention = null;
        }
        return intervention;
    }

    @Override
    protected JsonDocument entityToJsonDocument(Intervention entity) {
        MeanDAO meanDAO = new MeanDAO();
        JsonObject properties = JsonObject.create();
        properties.put("datatype", entity.getDataType());
        properties.put("address", entity.getAddress());
        properties.put("city", entity.getCity());
        properties.put("postcode", entity.getPostcode());
        properties.put("disasterCode", entity.getDisasterCode().toString());
        JsonArray means = JsonArray.create();
        for (Mean m : entity.getMeansList()) {
            means.add(meanDAO.entityToJsonObject(m));
        }
        JsonArray meansXtra = JsonArray.create();
        for (Mean mXtra : entity.getMeansXtra()) {
            meansXtra.add(meanDAO.entityToJsonObject(mXtra));
        }
        properties.put("meansList",means);
        properties.put("meansXtra",meansXtra);
        JsonObject jsonIntervention = JsonObject.empty()
                .put("type", "Point")
                .put("coordinates", Tools.positionToJsonArray(entity.getCoordinates()))
                .put("properties", properties);
        JsonDocument doc = JsonDocument.create("" + entity.getId(), jsonIntervention);
        return doc;
    }
}
