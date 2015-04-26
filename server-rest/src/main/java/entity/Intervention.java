package entity;

import java.util.Date;
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

	private Date dateCreate;
	private String label;
    private String address;
    private String postcode;
    private String city;
    private DisasterCode disasterCode;
    private List<Mean> meansList;

    private List<Mean> meansXtra;
    //private List<Mean> meansWaitingForApproval;
    private Position coordinates;

    public Intervention(String label, String address, String postcode, String city, DisasterCode disasterCode) {
        this();
        this.label = label;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.disasterCode = disasterCode;
        generateMeanList();
    }

    public Intervention() {
        super();
        this.dateCreate = new Date();
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
    
    
    public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


}

