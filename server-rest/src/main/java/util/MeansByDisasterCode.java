package util;

import entity.DisasterCode;
import entity.Mean;
import entity.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arno on 07/04/15.
 *
 * Utility for generating automatically the list of means according to a Disaster Code
 */
public class MeansByDisasterCode {

    public static List<Mean> meansByDisasterCode(DisasterCode d)
    {

        List<Mean> meanList = new ArrayList<Mean>();
        switch (d) {
            case SAP:
                meanList.add(new Mean(Vehicle.VSAV));
                break;
            case AVP:
                meanList.add(new Mean(Vehicle.VSAV));
                meanList.add(new Mean(Vehicle.VSR));
                meanList.add(new Mean(Vehicle.VLCG));
                break;
            case FHA:
                meanList.add(new Mean(Vehicle.FPT));
                meanList.add(new Mean(Vehicle.FPT));
                meanList.add(new Mean(Vehicle.EPA));
                meanList.add(new Mean(Vehicle.VLCG));
                break;
        }
        return meanList;
    }
}
