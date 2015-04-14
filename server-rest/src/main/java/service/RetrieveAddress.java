package service;

import com.google.maps.*;
import com.google.maps.model.GeocodingResult;

import entity.Position;

public class RetrieveAddress {

	private String address;
	private String ville;
	private String codepostal;
	
	private Position coordinates;

	
	public Position getCoordinates() {
		return coordinates;
	}



	public void setCoordinates(Position coordinates) {
		this.coordinates = coordinates;
	}



	public RetrieveAddress(String address, String codepostal , String ville) {
		super();
		this.address = address;
		this.ville = ville;
		this.codepostal = codepostal;
		
		setCoordinates(retrieveGps());
		System.out.println(coordinates.getLongitude());
		System.out.println(coordinates.getLatitude());

	}



	//https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=API_KEY
	//https://maps.googleapis.com/maps/api/geocode/json?address=Toledo&region=es&key=API_KEY
	//https://maps.googleapis.com/maps/api/geocode/json?address=santa+cruz&components=country:ES&key=API_KEY
	//https://maps.googleapis.com/maps/api/geocode/json?address=Torun&components=administrative_area:TX|country:US&key=API_KEY
	//https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=API_KEY
	//http://maps.googleapis.com/maps/api/geocode/json?parameters

	public Position retrieveGps(){
		Double lng = null;
		Double lat = null;
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyDcc_OP8XNo9jDmH5ooa_jrcYMPqMao4wY");
		GeocodingResult[] results = null;

		try {
			results = GeocodingApi.geocode(context,	address+" "+codepostal+" "+ville).await();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		lng = results[0].geometry.location.lng;
		lat = results[0].geometry.location.lat;
		return new Position(lng, lat);


	}

}
