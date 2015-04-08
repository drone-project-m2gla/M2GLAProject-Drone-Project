package dao;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import entity.DisasterCode;
import entity.GeoInterventionZone;
import entity.Intervention;
import entity.Mean;
import util.Constant;
import util.Tools;

/**
 * Created by alban on 13/03/15.
 */
public class InterventionDAO extends AbstractDAO<Intervention>{

    /**
     * Contructeur UnityDAO
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
                intervention.setPostcode(Integer.parseInt((String) ((JsonObject)content.get("properties")).get("postcode")));
                intervention.setCity((String) ((JsonObject)content.get("properties")).get("city"));
                intervention.setDisasterCode(DisasterCode.valueOf((String) ((JsonObject)content.get("properties")).get("disasterCode")));
                intervention.setMeansList(Tools.jsonArrayToMeanList(((JsonObject)content.get("properties")).getArray("meansList")));
                intervention.setCoordinates(Tools.jsonArrayToPosition(content.getArray("coordinates")));
            } else {
                throw new IllegalArgumentException();
            }
        }
        catch(Throwable t)
        {
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
        properties.put("meansList",means);
        JsonObject jsonIntervention = JsonObject.empty()
                .put("type", "Point")
                .put("coordinates", Tools.positionToJsonArray(entity.getCoordinates()))
                .put("properties", properties);
        JsonDocument doc = JsonDocument.create("" + entity.getId(), jsonIntervention);
        System.out.println(jsonIntervention);
        System.out.println(entity.getId());
        return doc;
    }
}
