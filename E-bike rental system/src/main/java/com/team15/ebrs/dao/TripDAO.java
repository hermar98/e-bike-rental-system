
package main.java.com.team15.ebrs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;

import main.java.com.team15.ebrs.data.BikeData;
import main.java.com.team15.ebrs.data.Trip;
import main.java.com.team15.ebrs.util.Calculations;
import main.java.com.team15.ebrs.util.DatabaseConnector;

/**
 * Data Access Object class for Trip. Provides necessary database interaction regarding the specified objects.
 * @author Team 15
 * @see Trip
 * @see DatabaseConnector
 *
 */

public class TripDAO { 
    private final String START = "INSERT INTO trip (customer_id, bike_id, time_start, start_station_id) VALUES (?,?,?,?)";
    private final String STOP = "UPDATE trip SET time_end = ?,  end_station_id = ?, trip_distance = ? WHERE trip_id = ?";
    private final String SELECT_BIKE_DATA_BY_TRIP_ID = "SELECT * FROM bike_data WHERE trip_id = ";
    private final String SELECT_TRIP_ID_BY_TIME_START = "SELECT trip_id FROM trip WHERE time_start = '";
    private final String CUSTOMER_ID = "' AND customer_id = '";
    private final String BIKE_ID = "' AND bike_id = '";
    private final String SELECT_TRIP_BY_TRIP_ID = "SELECT * FROM trip WHERE trip_id = ";
    private final String SELECT_ALL_TRIPS = "SELECT * FROM trip";
    private final String SELECT_ALL_TRIPS_BY_BIKE_ID = "SELECT * FROM trip WHERE bike_id = ";

    /**
     * Inserts a Trip object into the database with the necessary attributes to start a trip.
     *
     * Some attributes gets added later.
     * @param trip this parameter is a Trip object to add to the database.
     * @return  a boolean, true if it managed to add trip to database, false if it didn't manage.
     * @see Trip
     */
    public boolean startTrip(Trip trip){
        
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(START)){
            
            ps.setInt(1, trip.getPersonId());
            ps.setInt(2, trip.getBikeId());
            ps.setTimestamp(3, trip.getSqlStartTime());
            ps.setInt(4, trip.getStartStationId());
            ps.executeUpdate();
            
            return true;
        }catch(Exception e){
            System.out.println("Exception in startTrip: " + e);
        }
        return false;
    }

    /**
     * Updates the rest of the attributes for a trip, these are needed to register that the trip has ended.
     *
     * The trip is not complete in the database until it has ended/ has been updated with these attributes.
     * @param trip this parameter is a Trip object updated with all the attributes for a trip.
     * @return a boolean, true if it managed to update trip in the database, false if it didn't manage.
     * @see Trip
     */
    public boolean stopTrip(Trip trip){
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(STOP)){
            Calculations c = new Calculations();
            
            
            ps.setTimestamp(1, trip.getSqlEndTime());
            ps.setInt(2, trip.getEndStationId());
            ps.setDouble(3, c.getTotalDistance(getBikeDataForTrip(trip.getTripId())));
            ps.setInt(4, trip.getTripId());
            ps.executeUpdate();
            
            return true;
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
        return false;
    }

    /**
     * Returns the bike data for a specified trip.
     * @param tripId this parameter is the trip id for the requested trip in the database.
     * @return an ArrayList of BikeData.
     * @see BikeData
     * @see BikeDAO
     */
    public ArrayList<BikeData> getBikeDataForTrip (int tripId) {
        ArrayList<BikeData> bikeDataList = new ArrayList<>();
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_BIKE_DATA_BY_TRIP_ID + tripId);
            ResultSet rs = ps.executeQuery()){
            
            BikeDAO bDAO = new BikeDAO();
            while(rs.next()){
                bikeDataList.add(bDAO.getBikeDataFromRS(rs));
            }
        }catch(Exception e){
            System.out.println("Exception in getBikeDataForTrip: " + e);
        }
        return bikeDataList;
    }

    /**
     * Returns the trip id for a specified trip based on time the trip started customer id and bike id.
     *
     * Needed because the trip id is auto increment in the database.
     * @param trip this parameter is a Trip object with the needed variables.
     * @return an int for what the trip id is in the database.
     * @see Trip
     */
    public int getTripId(Trip trip){
        int tripId = -1;
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_TRIP_ID_BY_TIME_START + trip.getSqlStartTime() + CUSTOMER_ID + trip.getPersonId() + BIKE_ID + trip.getBikeId() + "'");
            ResultSet rs = ps.executeQuery()){   
            
            System.out.println("START " + trip.getSqlStartTime() + "PERS " + trip.getPersonId() + "BID " + trip.getBikeId());
            if(rs.next()){
               tripId = rs.getInt("trip_id");
            }         
        }catch(Exception e){
            System.out.println("EXCEPTION IN GET TRIP ID:  " + e);
        }
        return tripId;
    }

    /**
     * Returns a Trip object from the database with the corresponding trip id.
     * @param tripId this parameter specifies the trip id.
     * @return a Trip object with the specific trip id.
     * @see Trip
     */
    public Trip getTripById (int tripId) {
        Trip trip = null;

        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_TRIP_BY_TRIP_ID+tripId);
            ResultSet rs = ps.executeQuery()){
            if(rs.next()){
                int personId = rs.getInt("customer_id");
                int bikeId = rs.getInt("bike_id");
                LocalDateTime timeStart = rs.getTimestamp("time_start").toLocalDateTime();
                LocalDateTime timeEnd = rs.getTimestamp("time_end").toLocalDateTime();
                int startStationId = rs.getInt("start_station_id");
                int endStationId = rs.getInt("end_station_id");
                double tripDistance = rs.getDouble("trip_distance");

                trip = new Trip(bikeId,startStationId,personId,timeStart);
                trip.setTripId(tripId);
                trip.setEndStationId(endStationId);
                trip.setEndTime(timeEnd);
                trip.setTripDistance(tripDistance);
            }

        }catch(Exception e){
            System.out.println("Exception in getTrip:  " + e);
        }

        return trip;
    }

    /**
     * Returns an ArrayList of all Trip objects in the database.
     * @return an ArrayList of all Trip objects.
     * @see Trip
     */
    public ArrayList<Trip> getAllTrips () {
        ArrayList<Trip> trips = new ArrayList<>();

        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_ALL_TRIPS);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                int tripId = rs.getInt("trip_id");
                int personId = rs.getInt("customer_id");
                int bikeId = rs.getInt("bike_id");
                LocalDateTime timeStart = rs.getTimestamp("time_start").toLocalDateTime();
                LocalDateTime timeEnd = null;
                if(rs.getTimestamp("time_end")!=null) {
                    timeEnd = rs.getTimestamp("time_end").toLocalDateTime();
                }
                int startStationId = rs.getInt("start_station_id");
                int endStationId = rs.getInt("end_station_id");
                double tripDistance = rs.getDouble("trip_distance");

                Trip trip = new Trip(bikeId,startStationId,personId,timeStart);

                trip.setTripId(tripId);
                trip.setEndStationId(endStationId);
                trip.setEndTime(timeEnd);
                trip.setTripDistance(tripDistance);

                trips.add(trip);
            }

        }catch(Exception e){
            System.out.println("Exception in getAllTrips:  " + e);
        }
        return trips;
    }

    /**
     * Returns an ArrayList of all Trip objects in the database with the specified bike id.
     * @param bikeId this parameter specifies the bike id.
     * @return an ArrayList of all Trip objects with the specified bike id.
     * @see Trip
     */
    public ArrayList<Trip> getAllTripsByBikeId ( int bikeId) {
        ArrayList<Trip> trips = new ArrayList<>();

        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_ALL_TRIPS_BY_BIKE_ID + bikeId);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                int tripId = rs.getInt("trip_id");
                int personId = rs.getInt("customer_id");
                bikeId = rs.getInt("bike_id");
                LocalDateTime timeStart = rs.getTimestamp("time_start").toLocalDateTime();
                LocalDateTime timeEnd = null;

                if(rs.getTimestamp("time_end")!=null) {
                    timeEnd = rs.getTimestamp("time_end").toLocalDateTime();
                }

                int startStationId = rs.getInt("start_station_id");
                int endStationId = rs.getInt("end_station_id");
                double tripDistance = rs.getDouble("trip_distance");

                Trip trip = new Trip(bikeId,startStationId,personId,timeStart);

                trip.setTripId(tripId);
                trip.setEndStationId(endStationId);
                trip.setEndTime(timeEnd);
                trip.setTripDistance(tripDistance);

                trips.add(trip);
            }

        }catch(Exception e){
            System.out.println("Exception in getTrip:  " + e);
        }
        return trips;
    }
}
