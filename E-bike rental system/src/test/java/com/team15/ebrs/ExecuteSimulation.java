
package test.java.com.team15.ebrs;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.dao.CustomerDAO;
import main.java.com.team15.ebrs.dao.DockingStationDAO;
import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.Customer;
import main.java.com.team15.ebrs.data.DockingStation;
import static main.java.com.team15.ebrs.util.StationManager.UpdatePowerUsage;

public class ExecuteSimulation {
    public static void main(String args[]){
        Random rn = new Random();
        BikeDAO bDAO = new BikeDAO();
        ArrayList<Bike> bikeList = bDAO.getAllAvailableBikes();
        
        DockingStationDAO dDAO = new DockingStationDAO();
        ArrayList<DockingStation> dockList = dDAO.getAllDockingStations();
        
        CustomerDAO cDAO = new CustomerDAO();
        ArrayList<Customer> customerList = cDAO.getAllCustomers();
        
        int numberOfBikes = 100;
        int numberOfStations = dockList.size();
        int numberOfThreads = numberOfBikes + numberOfStations;
        ExecutorService executor= Executors.newFixedThreadPool(numberOfThreads);
        //Runtime.getRuntime().availableProcessors() for threadpool? NO, need 1 thread for each bike/trip
        try{
            for (int i=0; i < numberOfBikes; i++){
                if(bikeList.size() <= i){
                    break;
                }
                int randomCustomer = rn.nextInt(customerList.size());
                executor.execute(new BikeSimulator(bikeList.get(i), customerList.get(randomCustomer))); 
            }
            
            for(DockingStation d : dockList){
                executor.execute(new StationSimulator(d));
            }
            System.out.println("THREADS RUNNING: " + Thread.getAllStackTraces().keySet().size());
        }catch(Exception err){
            err.printStackTrace();
        }
        executor.shutdown(); // When simulation is done
    }   
}

class StationSimulator implements Runnable{
    private final int UPDATE_INTERVAL = 5;
    private final DockingStation dock;
    private int travellingBikes;
    private final BikeDAO bDAO = new BikeDAO();
    
    public StationSimulator(DockingStation dock){
        this.dock = dock;
    }
    
    @Override
    public void run(){
        try{
            do{
                UpdatePowerUsage(dock);
                TimeUnit.SECONDS.sleep(UPDATE_INTERVAL);
                travellingBikes = bDAO.getNumOfTravellingBikes();
            }while(travellingBikes != 0);
            
            UpdatePowerUsage(dock); //When all bikes are finished.
        }catch(Exception e){
            System.out.println("Exception in Station Simulation " + e);
        }
    }
}

class BikeSimulator implements Runnable{
    private final int UPDATE_INTERVAL = 5;
    private final Bike bike;
    private final Customer customer;
    private int travellingBikes;
    private final BikeDAO bDAO = new BikeDAO();
    
    public BikeSimulator(Bike bike, Customer customer){
        this.bike = bike;
        this.customer = customer;
    }
    @Override
    public void run(){
        try{    
            if(bike == null){
                return;
            }
            int startStation = bike.getStationId();
            DockingStation d;
            
            
            TestTrip trip = new TestTrip(bike, customer);
            trip.startSimulation();
            do{
                d = trip.simulateCycling();
                TimeUnit.SECONDS.sleep(UPDATE_INTERVAL);
                System.out.println("Jeg sykler nå! Min sykkel-id er: " + bike.getBikeId() + ". Jeg startet på stasjon: " + startStation);
            }while(d == null);
            
            do{
                trip.chargeBike();
                TimeUnit.SECONDS.sleep(UPDATE_INTERVAL);
                travellingBikes = bDAO.getNumOfTravellingBikes();
            }while(travellingBikes != 0);
            
            System.out.println("Jeg er ferdig nå!" + bike.getBikeId());
        }catch(Exception err){
            err.printStackTrace();
        }
    }
}