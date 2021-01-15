
package main.java.com.team15.ebrs.data;

import java.time.LocalDateTime;


/**
 * An object for BikeData. This is used to register data for a Bike object.
 *
 * @author Team 15
 * @see LocalDateTime
 */
public class BikeData {
    private final int bikeId;
    private LocalDateTime dateTime;
    private double latitude;
    private double longitude;
    private double chargingLvl;

    /**
     * This it the constructor for the BikeData object.
     *
     * It takes a bike id, a LocalDateTime object, a latitude, a longitude and a charging level.
     * @param bikeId
     * @param dateTime
     * @param latitude
     * @param longitude
     * @param chargingLvl
     */
    public BikeData(int bikeId, LocalDateTime dateTime, double latitude, double longitude, double chargingLvl){
        this.bikeId = bikeId;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.chargingLvl = chargingLvl;
    }

    /**
     * Returns the bike id for the BikeData object.
     * @return the bike id for the BikeData object.
     */
    public int getBikeId(){
        return bikeId;
    }

    /**
     * Returns the LocalDateTime object for the BikeData object.
     * @return the LocalDateTime object for the BikeData object.
     * @see LocalDateTime
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns a sql Timestamp object of the LocalDateTime object.
     * @return a sql Timestamp object of the LocalDateTime object.
     * @see java.sql.Timestamp
     */
    public java.sql.Timestamp getSqlDateTime(){
         return java.sql.Timestamp.valueOf(dateTime);
    }

    /**
     * Sets the LocalDateTime object for the BikeData object.
     * @param newDateTime this is the LocalDateTime object you want it set to.
     * @see LocalDateTime
     */
    public void setDateTime(LocalDateTime newDateTime) {
        dateTime = newDateTime;
    }

    /**
     * Returns the latitude for the BikeData object.
     * @return the latitude for the BikeData object.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude for the BikeData object.
     * @param newLatitude the latitude you want it set to.
     */
    public void setLatitude(double newLatitude) {
        latitude = newLatitude;
    }

    /**
     * Returns the longitude for the BikeData object.
     * @return the longitude for the BikeData object.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude for the BikeData object.
     * @param newLongitude the longitude you want it set to.
     */
    public void setLongitude(double newLongitude) {
        longitude = newLongitude;
    }

    /**
     * Returns the charging level of the BikeData object.
     * @return the charging level of the BikeData object.
     */
    public double getChargingLvl() {
        return chargingLvl;
    }

    /**
     * Sets the charging level for the BikeData object.
     * @param newChargingLvl the charging level you want it set to.
     */
    public void setChargingLvl(double newChargingLvl) {
        chargingLvl = newChargingLvl;
    }

    /**
     * Returns a String with all the information about the BikeData object.
     * @return a String with all the information about the BikeData object.
     */
    public String toString(){
        
        return String.format("%-2s\t%-30s\t%-10s\t%-8s\t%-8s", bikeId, dateTime, latitude, longitude, chargingLvl + "%");
    }
}
