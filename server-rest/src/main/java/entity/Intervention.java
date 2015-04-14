package entity;

import java.util.List;
import java.util.ArrayList;

import util.Constant;
import util.MeansByDisasterCode;


/**
 * Created by arno on 07/04/15.
 *
 * This class is used for handling intervention information.
 *
 */

public class Intervention extends AbstractEntity {

    private String address;
    private String postcode;
    private String city;
    private DisasterCode disasterCode;
    private List<Mean> meansList;

    private List<Mean> meansXtra;
    //private List<Mean> meansWaitingForApproval;
    private Position coordinates;

    public Intervention(String address, String postcode, String city, DisasterCode disasterCode) {
        this();
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.disasterCode = disasterCode;
        generateMeanList();
    }

    public Intervention() {
        super();
        this.datatype = Constant.DATATYPE_INTERVENTION;
        this.meansXtra = new ArrayList<Mean>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public DisasterCode getDisasterCode() {
        return disasterCode;
    }

    public void setDisasterCode(DisasterCode disasterCode) {
        this.disasterCode = disasterCode;
    }

    public List<Mean> getMeansList() {
        return meansList;
    }

    public void setMeansList(List<Mean> meansList) {
        this.meansList = meansList;
    }

    public Position getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Position coordinates) {
        this.coordinates = coordinates;
    }

    public void generateMeanList(){
        if (this.disasterCode != null) {
            this.meansList = MeansByDisasterCode.meansByDisasterCode(disasterCode);
        }
    }


    public List<Mean> getMeansXtra() {
        return meansXtra;
    }

    public void setMeansXtra(List<Mean> meansXtra) {
        this.meansXtra = meansXtra;
    }


}

