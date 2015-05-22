package util;

import java.util.ArrayList;
import java.util.List;
import com.mongodb.BasicDBList;
import entity.*;
import org.bson.Document;

/**
 * Created by alban on 10/03/15.
 */
public class Tools {

    public static BasicDBList positionToBasicDBList(Position p)
    {
        BasicDBList basicDBList = new BasicDBList();
        basicDBList.add(p.getLatitude());
        basicDBList.add(p.getLongitude());
        basicDBList.add(p.getAltitude());
        return basicDBList;
    }

    public static Position arrayListToPosition(ArrayList arrayList)
    {
        Position p = new Position();
        if(arrayList !=null || arrayList.size() != 3)
        {
            p.setLatitude((Double) arrayList.get(0));
            p.setLongitude((Double) arrayList.get(1));
            p.setAltitude((Double) arrayList.get(2));
        }
        return p;
    }

    public static Zone basicDBListToZone(BasicDBList listDocuments) {
        Zone z = new Zone();
        for(int i=0; i<listDocuments.size();i++) {
            z.addPosition(Tools.arrayListToPosition((BasicDBList) listDocuments.get(i)));
        }
        return z;
    }

    public static BasicDBList zoneToBasicDBList(Zone zone) {
        BasicDBList res = new BasicDBList();
        for(Position p : zone.getPositions()) {
            res.add(Tools.positionToBasicDBList(p));
        }
        return res;
    }

    public static List<Zone> arrayListToZoneList(ArrayList arrayList) {
        List<Zone> z = new ArrayList<Zone>();
        for(int i=0; i<arrayList.size();i++) {
            z.add(Tools.basicDBListToZone((BasicDBList)arrayList.get(i)));
        }
        return z;
    }

    public static BasicDBList zoneListToBasicDBList(List<Zone> zones) {
        BasicDBList res = new BasicDBList();
        for(Zone zone : zones) {
            res.add(Tools.zoneToBasicDBList(zone));
        }
        return res;
    }

    public static List<Mean> documentListToMeanList(List<Document> documents) {
        List<Mean> z = new ArrayList<Mean>();
        for(int i=0; i<documents.size();i++) {
            z.add(documentToMean(documents.get(i)));
        }
        return z;
    }

    public static BasicDBList meanListToBasicDBList(List<Mean> means) {
        BasicDBList basicDBList = new BasicDBList();
        for(Mean mean : means) {
            basicDBList.add(Tools.meanToDocument(mean));
        }
        return basicDBList;
    }

    public static Mean documentToMean(Document document) {
        if(document==null)
        {
            return null;
        }
        Mean mean = new Mean();
        mean.setId(document.getLong("_id"));
        mean.setName(document.getString("name"));
        mean.setVehicle(Vehicle.valueOf(document.getString("vehicle")));
        mean.setInPosition(document.getBoolean("inPosition"));
        mean.setMeanState(MeanState.valueOf(document.getString("meanState")));
        mean.setDateRequested(document.getDate("dateRequested"));
        mean.setDateActivated(document.getDate("dateActivated"));
        mean.setDateArrived(document.getDate("dateArrived"));
        mean.setDateEngaged(document.getDate("dateEngaged"));
        mean.setDateReleased(document.getDate("dateReleased"));
        mean.setDateRefused(document.getDate("dateRefused"));
        mean.setCoordinates(Tools.arrayListToPosition((ArrayList) document.get("coordinates")));
        return mean;
    }

    public static Document meanToDocument(Mean entity) {
        Document jsonMean = new Document();
        jsonMean.put("name", entity.getName());
        jsonMean.put("inPosition", entity.getInPosition());
        jsonMean.put("meanState", entity.getMeanState().toString());
        jsonMean.put("vehicle", entity.getVehicle().toString());
        jsonMean.put("dateRequested",entity.getDateRequested());
        jsonMean.put("dateActivated",entity.getDateActivated());
        jsonMean.put("dateArrived",entity.getDateArrived());
        jsonMean.put("dateEngaged",entity.getDateEngaged());
        jsonMean.put("dateReleased",entity.getDateReleased());
        jsonMean.put("dateRefused",entity.getDateRefused());
        jsonMean.put("_id", entity.getId());
        jsonMean.put("type", "Point");
        jsonMean.put("coordinates", Tools.positionToBasicDBList(entity.getCoordinates()));
        return jsonMean;
    }

    public static boolean isSamePositions(Position p1, Position p2)
    {
        return equivalentInGPS(p1.getLatitude(),p2.getLatitude()) && equivalentInGPS(p1.getLongitude(),p2.getLongitude());
    }

    public static boolean equivalentInGPS(double gps1, double gps2)
    {
        int i1 = (int) (gps1 * 100000);
        int i2 = (int) (gps2 * 100000);
        return i1 == i2;
    }

}