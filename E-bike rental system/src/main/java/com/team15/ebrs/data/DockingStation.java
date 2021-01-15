package main.java.com.team15.ebrs.data;

/**
 * An object for DockingStation. This is used to register different docking stations.
 *
 * @author Team 15
 */

public class DockingStation {
	
	private int stationId;
	private String stationName;
	private double coordinatelat;
	private double coordinatelng;
	private int total_docks;

	/**
	 *This constructor of the DockingStation class contains all the information of the docking station.
	 *
	 * @param stationId     the station id is the primary key of the docking station
	 * @param stationName   the name of the docking station
	 * @param coordinatelat the latitude coordinate of the station
	 * @param coordinatelng the long coordinate of the station
	 * @param total_docks   the number of the docks on the station
	 */
	public DockingStation(int stationId, String stationName,double coordinatelat,double coordinatelng,int total_docks) {
		this.stationId = stationId;
		this.stationName = stationName;
		this.coordinatelat = coordinatelat;
		this.coordinatelng = coordinatelng;
		this.total_docks=total_docks;
	}

	/**
	 *This constructor of the DockingStation class is used for creating a new docking station on database, and the station_id will be automatically default. 
	 *	
	 * @param stationName 	the name of the docking station
	 * @param coordinatelat  the latitude coordinate of the station
	 * @param coordinatelng  the long coordinate of the station
	 * @param total_docks 	 the number of the docks on the station
	 */
	public DockingStation(String stationName,double coordinatelat,double coordinatelng,int total_docks) {
		this.stationName=stationName;
		this.coordinatelat = coordinatelat;
		this.coordinatelng = coordinatelng;
		this.total_docks=total_docks;
	}

	/**
	 * Returns the station id for the DockingStation object.
	 * @return the station id for the DockingStation object.
	 */
	public int getStationId() {
		return stationId;
	}

	/**
	 * 	Returns the name of the station 	
	 *
	 * @return the name of the station
	 */
	public String getStationName() {
		return stationName;
	}

	/**
	 *Returns the latitude coordinate of the station 
	 *
	 * @return the latitude coordinate of the station 
	 */
	public double getCoordinateLat() {
		return coordinatelat;
	}

	/**
	 *Returns the long coordinate of the station 
	 *
	 * @return the long coordinate of the station
	 */
	public double getCoordinateLng() {
		return coordinatelng;
	}

	/**
	 *Returns the number of the docks on station
	 *
	 * @return the number of the docks on the station 
	 */
	public int getAntDocks() { 
		return total_docks;
	}


	/**
	 *Changes the name of the docking station 
	 *
	 * @param stationName - the new name for the docking station
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	/**
	 *Changes the coordinate system of the docking station 
	 *
	 * @param coordinatelat the new latitude coordinate of the station
	 * @param coordinatelng the new long coordinate of the station
	 */
	public void setNyCoordinaten(double coordinatelat,double coordinatelng) {
		this.coordinatelat=coordinatelat;
		this.coordinatelng=coordinatelng;
	}

	/**
	 *Changes the number of docks on the docking station
	 *
	 * @param antDocks the number of docks
	 */
	public void setAntDocks(int antDocks) {
		this.total_docks = antDocks;
	}

	/**
	 * Returns a string representation of this collection
	 *
	 * @return a string representation of the docking station 
	 */
	public String toString() {
		return "Station ID: " + stationId + " Station Name: " +stationName + " Coordinatelat: "+ coordinatelat +" Coordinatelng: " + coordinatelng + " Total docks: "+total_docks;
	}
}
