
package main.java.com.team15.ebrs.util;

import java.time.LocalDateTime;
import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.dao.DockingStationDAO;
import main.java.com.team15.ebrs.dao.TripDAO;
import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.BikeData;
import main.java.com.team15.ebrs.data.Customer;
import main.java.com.team15.ebrs.data.DockingStation;
import main.java.com.team15.ebrs.data.Trip;

/**
 * Class intended to be used by the ones creating the automated payment stations. This also includes methods used in the simulation.
 * @author Team15.
 */
public class TripManager {
    private Trip trip;
    private final Customer customer;
    private final TripDAO trDAO = new TripDAO();
    private final BikeDAO bDAO = new BikeDAO();
    private final Bike bike;
    
    /**
     * Constructor for the class needed to start a trip. Bike object and a customer needed to create this object.
     * @param bike The chosen bike that is going on the trip
     * @param customer The customer that is paying/using the bike
     */
    public TripManager(Bike bike, Customer customer){
        this.bike = bike;
        this.customer = customer;
    }
    
    /**
     * Starts the trip, will be started as soon as a Bike leaves the station.
     */
    public void startTrip(){
        trip = new Trip(bike.getBikeId(), bike.getStationId(), customer.getCustomerId(), LocalDateTime.now());
        trDAO.startTrip(trip);
        trip.setTripId(trDAO.getTripId(trip));
        
        bike.setStationId(0);
        bDAO.editBike(bike);
    }
    
    /**
     * Used to update the coordinates for the bike at the trip.
     * @param bikeData BikeData object containing the current coordinates and charging level for the bike.
     */
    public void updateCoordinates(BikeData bikeData){
        bDAO.addBikeData(bikeData, trip.getTripId());
    }
    
    public int getTripId(){
        return trip.getTripId();
    }
    
    /**
     * Used to end a trip. Will be automaticly executed when the bike enters a docking station.
     * @param dock The docking station the bike is delivered at
     */
    public void endTrip(DockingStation dock){
        bike.setStationId(dock.getStationId());
        bDAO.editBike(bike);
        
        trip.setEndStationId(bike.getStationId());
        trip.setEndTime(LocalDateTime.now());
        trDAO.stopTrip(trip);
    } 
}
