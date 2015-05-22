package entity;

import java.util.Date;
import java.util.List;

import util.Datetime;
import util.MeansByDisasterCode;


/**
 * @author arno
 * @see Intervention is used for handling intervention information.
 */

public class Intervention extends AbstractEntity {

	private Date dateCreate;
	private String label;
    private String address;
    private String postcode;
    private String city;
    private DisasterCode disasterCode;
    private List<Mean> meansList;
   // private List<Mean> meansXtra;
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
        this.dateCreate = Datetime.getCurrentDate();
       // this.meansXtra = new ArrayList<Mean>();
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


//    public List<Mean> getMeansXtra() {
//        return meansXtra;
//    }
//
//    public void setMeansXtra(List<Mean> meansXtra) {
//        this.meansXtra = meansXtra;
//    }


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

    @Override
    public String toString() {
        return "Intervention{" +
                "id=" + id +
                ", dateCreate=" + dateCreate +
                ", label='" + label + '\'' +
                ", address='" + address + '\'' +
                ", postcode='" + postcode + '\'' +
                ", city='" + city + '\'' +
                ", disasterCode=" + disasterCode +
                ", meansList=" + meansList +
              //  ", meansXtra=" + meansXtra +
                ", coordinates=" + coordinates +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intervention)) return false;
        Intervention that = (Intervention) o;
        if (id != that.id) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (coordinates != null ? !coordinates.equals(that.coordinates) : that.coordinates != null) return false;
        if (dateCreate != null ? !dateCreate.equals(that.dateCreate) : that.dateCreate != null) return false;
        if (disasterCode != that.disasterCode) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (meansList != null ? !meansList.equals(that.meansList) : that.meansList != null) return false;
    //    if (meansXtra != null ? !meansXtra.equals(that.meansXtra) : that.meansXtra != null) return false;
        if (postcode != null ? !postcode.equals(that.postcode) : that.postcode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dateCreate != null ? dateCreate.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (postcode != null ? postcode.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (disasterCode != null ? disasterCode.hashCode() : 0);
        result = 31 * result + (meansList != null ? meansList.hashCode() : 0);
      //  result = 31 * result + (meansXtra != null ? meansXtra.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }
}

