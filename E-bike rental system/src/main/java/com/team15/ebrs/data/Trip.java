
package main.java.com.team15.ebrs.data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * An object for Trip. This is used to register different Trip objects
 *
 * @author Team 15
 * @see LocalDateTime
 * @see Timestamp
 */

public class Trip {
    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private int tripId;
    private final int bikeId;
    private final int personId;
    private final int startStationId;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private int endStationId;
    private double tripDistance;

    /**
     * The constructor of the Trip object.
     * @param bikeId
     * @param startStationId
     * @param personId
     * @param startTime
     */
    public Trip(int bikeId, int startStationId, int personId, LocalDateTime startTime){
        this.bikeId = bikeId;
        this.personId = personId;
        this.startTime = startTime;
        this.startStationId = startStationId;
    }

    /**
     * Returns the bike id of the Trip object.
     * @return the bike id of the Trip object.
     */
    public int getBikeId() {
        return bikeId;
    }

    /**
     * Returns the person id of the Trip object.
     * @return the person id of the Trip object.
     */
    public int getPersonId(){
        return personId;
    }

    /**
     * Returns the starting station id for the Trip object.
     * @return the starting station id for the Trip object.
     */
    public int getStartStationId(){
        return startStationId;
    }

    /**
     * Returns the LocalDateTime object of the starting time for the Trip object.
     * @return the LocalDateTime object of the starting time for the Trip object.
     * @see LocalDateTime
     */
    public LocalDateTime getStartTime(){
        return startTime;
    }

    /**
     * Returns the Timestamp of the starting time for the Trip object.
     * @return the Timestamp of the starting time for the Trip object.
     * @see Timestamp
     */
    public Timestamp getSqlStartTime(){
        return Timestamp.valueOf(startTime.withNano(0));
    }

    /**
     * Returns the Timestamp of the end time for the Trip object.
     * @return the Timestamp of the end time for the Trip object.
     * @see Timestamp
     */
    public Timestamp getSqlEndTime(){
        return Timestamp.valueOf(endTime.withNano(0));
    }

    /**
     * Returns an int for the trip id.
     * @return an int for the trip id.
     */
    public int getTripId() {
        return tripId;
    }

    /**
     * Sets the trip id for the Trip object.
     * @param tripId the trip id you want it set to.
     */
    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    /**
     * Returns the LocalDateTime object of the end time for the Trip object.
     * @return the LocalDateTime object of the end time for the Trip object.
     * @see LocalDateTime
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the LocalDateTime object of the end time for the Trip object.
     * @param endTime the LocalDateTime object you want it set to.
     *
     *
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the end station id for the Trip object.
     * @return the end station id for the Trip object.
     */
    public int getEndStationId() {
        return endStationId;
    }

    /**
     * Sets the end station id for the Trip object.
     * @param endStationId the end station id you want it set to.
     */
    public void setEndStationId(int endStationId) {
        this.endStationId = endStationId;
    }

    /**
     * Sets the trip distance for the Trip object.
     * @param tripDistance the trip distance you want it set to.
     */
    public void setTripDistance (double tripDistance)  {
        this.tripDistance = tripDistance;
    }

    /**
     * Returns the trip distance for the Trip object.
     * @return the trip distance for the Trip object.
     */
    public double getTripDistance() {
        return tripDistance;
    }
}
