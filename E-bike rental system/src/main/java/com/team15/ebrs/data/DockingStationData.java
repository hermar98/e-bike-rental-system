
package main.java.com.team15.ebrs.data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * An object for DockingStationData. This is used to register different DockingStationData.
 *
 * @author Team 15
 * @see LocalDateTime
 */
public class DockingStationData {
    private int stationId;
    private LocalDateTime dateTime;
    private double powerUsage;

    /**
     * This is the constructor of the DockingStationData object. It takes station id, datetime and power usage as parameters.
     * @param stationId the station id wanted.
     * @param dateTime the datetime of the data.
     * @param powerUsage the current power usage of the station.
     */
    public DockingStationData(int stationId, LocalDateTime dateTime, double powerUsage){
        this.stationId = stationId;
        this.dateTime = dateTime.withNano(0);
        this.powerUsage = powerUsage;
    }

    /**
     * Returns the station id of the DockingStationData object.
     * @return the station id of the DockingStationData object.
     */
    public int getStationId() {
        return stationId;
    }

    /**
     * Sets the station id for the DockingStationData object.
     * @param stationId the station id you want to set it to.
     */
    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    /**
     * Returns the LocalDateTime object of the DockingStationData object.
     * @return the LocalDateTime object of the DockingStationData object.
     * @see LocalDateTime
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns the Timestamp object of the DockingStationData object.
     * @return the Timestamp object of the DockingStationData object.
     * @see Timestamp
     */
    public Timestamp getSQLDateTime(){
        return Timestamp.valueOf(dateTime);
    }

    /**
     * Sets the LocalDateTime object of the DockingStationData object.
     * @param dateTime the LocalDateTime object you want it set to.
     * @see LocalDateTime
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Returns the power usage of the DockingStationData object.
     * @return the power usage of the DockingStationData object.
     */
    public double getPowerUsage() {
        return powerUsage;
    }

    /**
     * Sets the power usage for the DockingStationData object.
     * @param powerUsage the power usage you want it set to.
     */
    public void setPowerUsage(double powerUsage) {
        this.powerUsage = powerUsage;
    } 
}
