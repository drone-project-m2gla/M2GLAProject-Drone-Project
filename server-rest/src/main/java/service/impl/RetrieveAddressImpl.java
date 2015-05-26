package service.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import entity.Position;
import service.RetrieveAddress;

/**
 * RetrieveAddressImpl permits to use GeocodingApi
 * Api can retrieve coordinates with postal address
 * @author jeremie
 */
public class RetrieveAddressImpl implements RetrieveAddress {

	private String address;
	private String ville;
	private String codepostal;
	private Position coordinates;

	@Override
	public Position getCoordinates() {
		return coordinates;
	}

	@Override
	public void setCoordinates(Position coordinates) {
		this.coordinates = coordinates;
	}

	public RetrieveAddressImpl(String address, String codepostal , String ville) {
		super();
		this.address = address;
		this.ville = ville;
		this.codepostal = codepostal;
		
		setCoordinates(retrieveGps());
		System.out.println(coordinates.getLongitude());
		System.out.println(coordinates.getLatitude());

	}

	/**
	 * Exemples :
	 * <ul>
		<li>https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=API_KEY</li>
		<li>https://maps.googleapis.com/maps/api/geocode/json?address=Toledo&region=es&key=API_KEY</li>
		<li>https://maps.googleapis.com/maps/api/geocode/json?address=santa+cruz&components=country:ES&key=API_KEY</li>
		<li>https://maps.googleapis.com/maps/api/geocode/json?address=Torun&components=administrative_area:TX|country:US&key=API_KEY</li>
		<li>https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=API_KEY</li>
		<li>http://maps.googleapis.com/maps/api/geocode/json?parameters</ul>
	 * </ul>
	 */
	@Override
	public Position retrieveGps(){
		Double lng = null;
		Double lat = null;
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyDlSqghzy1zZTnKMG0Wwc0h8neFnDs5pog");
		GeocodingResult[] results = null;

		try {
			results = GeocodingApi.geocode(context,	address+" "+codepostal+" "+ville).await();
		} catch (Exception e) {
			e.printStackTrace();
		}

		lng = results[0].geometry.location.lng;
		lat = results[0].geometry.location.lat;
		return new Position(lng, lat);
	}
}
