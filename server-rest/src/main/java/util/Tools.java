package util;

import java.util.ArrayList;
import java.util.List;
import com.mongodb.BasicDBList;
import entity.Mean;
import entity.Position;
import entity.Vehicle;
import entity.Zone;
import org.bson.Document;

/**
 * Created by alban on 10/03/15.
 */
public class Tools {

    public static Document positionToDocument(Position p)
    {
        Document document = new Document();
        document.put("latitude", p.getLatitude());
        document.put("longitude",p.getLongitude());
        document.put("altitude", p.getAltitude());
        return document;
    }

    public static Position documentToPosition(Document document)
    {
        Position p = new Position();

        if (document.getDouble("latitude") == null || document.getDouble("latitude").equals("NaN")){
            p.setLatitude(Double.NaN);
        }
        else
        {
            p.setLatitude(document.getDouble("latitude"));
        }

        if (document.getDouble("longitude") == null || document.getDouble("longitude").equals("NaN")){
            p.setLongitude(Double.NaN);
        }
        else
        {
            p.setLongitude(document.getDouble("longitude"));
        }

        if (document.get("altitude") ==null || document.get("altitude").equals("NaN")){
            p.setAltitude(Double.NaN);
        }
        else
        {
            p.setAltitude(document.getDouble("altitude"));
        }
        return p;
    }

    public static Zone basicDBListToZone(BasicDBList listDocuments) {
        Zone z = new Zone();
        for(int i=0; i<listDocuments.size();i++) {
            z.addPosition(Tools.documentToPosition((Document) listDocuments.get(i)));
        }
        return z;
    }

    public static BasicDBList zoneToBasicDBList(Zone zone) {
        BasicDBList res = new BasicDBList();
        for(Position p : zone.getPositions()) {
            res.add(Tools.positionToDocument(p));
        }
        return res;
    }

    public static List<Zone> basicDBListToZoneList(BasicDBList basicDBList) {
        List<Zone> z = new ArrayList<Zone>();
        for(int i=0; i<basicDBList.size();i++) {
            z.add(Tools.basicDBListToZone((BasicDBList)basicDBList.get(i)));
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
        Mean Mean = new Mean();
        Mean.setId(document.getLong("_id"));
        Mean.setVehicle(Vehicle.valueOf(document.getString("vehicle")));
        Mean.setInPosition(document.getBoolean("inPosition"));
        Mean.setisDeclined(document.getBoolean("isDeclined"));
        Mean.setCoordinates(Tools.documentToPosition((Document) document.get("coordinates")));
        return Mean;
    }

    public static Document meanToDocument(Mean entity) {
        Document jsonMean = new Document();
        jsonMean.put("inPosition", entity.getInPosition());
        jsonMean.put("isDeclined", entity.getisDeclined());
        jsonMean.put("vehicle", entity.getVehicle().toString());
        jsonMean.put("_id", entity.getId());
        jsonMean.put("type", "Point");
        jsonMean.put("coordinates", Tools.positionToDocument(entity.getCoordinates()));
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