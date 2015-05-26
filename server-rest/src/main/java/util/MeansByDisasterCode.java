package util;

import java.util.ArrayList;
import java.util.List;

import entity.DisasterCode;
import entity.Mean;
import entity.Vehicle;

/**
 * Created by arno on 07/04/15.
 * Utility for generating automatically the list of means according to a Disaster Code
 */
public class MeansByDisasterCode {

    public static List<Mean> meansByDisasterCode(DisasterCode d)
    {

        List<Mean> meanList = new ArrayList<Mean>();
        switch (d) {
            case SAP:
                meanList.add(new Mean(Vehicle.VSAV,"Init",true));
                break;
            case AVP:
                meanList.add(new Mean(Vehicle.VSAV,"Init",true));
                meanList.add(new Mean(Vehicle.VSR,"Init",true));
                meanList.add(new Mean(Vehicle.VLCG,"Init",true));
                break;
            case FHA:
                meanList.add(new Mean(Vehicle.FPT,"Init",true));
                meanList.add(new Mean(Vehicle.FPT,"Init",true));
                meanList.add(new Mean(Vehicle.EPA,"Init",true));
                meanList.add(new Mean(Vehicle.VLCG,"Init",true));
                break;
        }
        return meanList;
    }
}
