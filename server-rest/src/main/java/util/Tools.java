package util;

import java.util.ArrayList;
import java.util.List;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import entity.Mean;
import entity.Position;
import entity.Vehicle;
import entity.Zone;

/**
 * Created by alban on 10/03/15.
 */
public class Tools {

    public static JsonArray positionToJsonArray(Position p)
    {
        JsonArray jsonArray = JsonArray.create();
        jsonArray.add(p.getLatitude());
        jsonArray.add(p.getLongitude());
        jsonArray.add(p.getAltitude());
        return jsonArray;
    }

    public static Position jsonArrayToPosition(JsonArray jsonArray)
    {
        Position p = new Position();
        p.setLatitude((Double) jsonArray.get(0));
        p.setLongitude((Double)jsonArray.get(1));

        if (jsonArray.get(2).equals("NaN")){
            p.setAltitude(Double.NaN);
        }
        else
        {
            p.setAltitude((Double)jsonArray.get(2));
        }
        return p;
    }

    public static Zone jsonArrayToZone(JsonArray jsonArray) {
        Zone z = new Zone();
        for(int i=0; i<jsonArray.size();i++) {
            z.addPosition(Tools.jsonArrayToPosition((JsonArray) jsonArray.get(i)));
        }
        return z;
    }

    public static JsonArray zoneToJsonArray(Zone zone) {
        JsonArray array = JsonArray.create();
        for(Position p : zone.getPositions()) {
            array.add(Tools.positionToJsonArray(p));
        }
        return array;
    }

    public static List<Zone> jsonArrayToZoneList(JsonArray jsonArray) {
        List<Zone> z = new ArrayList<Zone>();
        for(int i=0; i<jsonArray.size();i++) {
            z.add(Tools.jsonArrayToZone((JsonArray) jsonArray.get(i)));
        }
        return z;
    }

    public static JsonArray zoneListToJsonArray(List<Zone> zones) {
        JsonArray array = JsonArray.create();
        for(Zone zone : zones) {
            array.add(Tools.zoneToJsonArray(zone));
        }
        return array;
    }

    public static List<Mean> jsonArrayToMeanList(JsonArray jsonArray) {
        List<Mean> z = new ArrayList<Mean>();
        for(int i=0; i<jsonArray.size();i++) {
            z.add(jsonToMean((JsonObject) jsonArray.get(i)));

        }
        return z;
    }

    public static JsonArray meanListToJsonArray(List<Mean> means) {
        JsonArray array = JsonArray.create();
        for(Mean mean : means) {
            array.add(Tools.meanToJsonArray(mean));
        }
        return array;
    }


    public static JsonArray meanToJsonArray(Mean mean) {
        JsonArray array = JsonArray.create();
        array.add(Tools.positionToJsonArray(mean.getCoordinates()));
        return array;
    }

    public static Mean jsonToMean(JsonObject jsonObject) {
        Mean z = new Mean();
        z.setId(((JsonObject) jsonObject.get("properties")).getLong("id"));
        z.setVehicle(Vehicle.valueOf(((JsonObject) jsonObject.get("properties")).getString("vehicle")));
        z.setInPosition(((JsonObject) jsonObject.get("properties")).getBoolean("inPosition"));
        z.setCoordinates(Tools.jsonArrayToPosition(jsonObject.getArray("coordinates")));
        return z;
    }

}