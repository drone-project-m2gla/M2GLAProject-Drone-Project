package service;

import entity.Position;

public interface RetrieveAddress {
	public Position getCoordinates();
	public void setCoordinates(Position coordinates);
	public Position retrieveGps();
}
