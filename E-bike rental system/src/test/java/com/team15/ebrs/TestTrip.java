package test.java.com.team15.ebrs;

import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.dao.DockingStationDAO;
import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.BikeData;
import main.java.com.team15.ebrs.data.DockingStation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import main.java.com.team15.ebrs.dao.TripDAO;
import main.java.com.team15.ebrs.data.Customer;
import main.java.com.team15.ebrs.util.TripManager;

public class TestTrip {
    TripManager tManager;
    Random rn = new Random();
    
    Bike bike;
    BikeData bikeData;

    BikeDAO bikeDAO = new BikeDAO();
    TripDAO tDAO = new TripDAO();
    DockingStationDAO stationDAO = new DockingStationDAO();

    DockingStation prevStation;
    DockingStation randomStation;

    public TestTrip (Bike bike, Customer customer){
        tManager = new TripManager(bike, customer);
        this.bike = bike;
        ArrayList<DockingStation> stations = stationDAO.getAllDockingStations();
        for (int i = 0; i < stations.size(); i++) {
            if(bike.getStationId()==stations.get(i).getStationId()){
                prevStation = stations.get(i);
                break;
            }else{
                prevStation = null;
            }
        }
        randomStation = findRandomStation();
    }
    
    public void startSimulation(){
        tManager.startTrip();
    }

    //Cycling, coords & charge lvl every minute
    public DockingStation simulateCycling () {
        //System.out.println(bike);
        bikeData = bikeDAO.getRecentData(bike);
        //System.out.println(bikeData);
        bikeData.setDateTime(LocalDateTime.now());

        double latCoords = bikeData.getLatitude();
        double longCoords = bikeData.getLongitude();

        //powerusage down
        double d = 1000;
        double powerUsage = (Math.round(((Math.random()*20+1)/10)*d))/d;
        bikeData.setChargingLvl((bikeData.getChargingLvl()-powerUsage));

        //new  random coords
        double randomMovementLat = (Math.random()*1)/200; //100 standard
        double randomMovementLong = (Math.random()*1)/200;

        double closeEnough = 0.002;

        double diffLat = randomStation.getCoordinateLat()-bikeData.getLatitude();
        double diffLong = randomStation.getCoordinateLng()-bikeData.getLongitude();


        if(Math.abs(diffLat)>closeEnough) {
            if (diffLat > 0) {
                latCoords += randomMovementLat;
            } else if (diffLat < 0) {
                latCoords -= randomMovementLat;
            }
        }
        bikeData.setLatitude(latCoords);

        if(Math.abs(diffLong)>closeEnough) {
            if (diffLong > 0) {
                longCoords += randomMovementLong;
            } else if (diffLong < 0) {
                longCoords -= randomMovementLong;
            }
        }
        bikeData.setLongitude(longCoords);

        //Add entry to database
        tManager.updateCoordinates(bikeData);

        System.out.println("Bikeid: "+ bike.getBikeId() +"\ndiffLat: " + diffLat + "\ndiffLong: " + diffLong);
        System.out.println("Going to: " + randomStation.getStationName() + " ID: " + randomStation.getStationId());
        
        if(Math.abs(diffLat)<=closeEnough && Math.abs(diffLong)<=closeEnough){
            return simulateCheckIn();
        }
        return null;
    }

    //Checkin Power Usage UP set Station ID
    public DockingStation simulateCheckIn () {
        tManager.endTrip(randomStation);
        return randomStation;
    }
    
    public void chargeBike(){
        double STANDARD_CHARGING_VALUE = 1.3;
        double chargingLvl = bikeData.getChargingLvl() + STANDARD_CHARGING_VALUE;
        if(chargingLvl > 100){
            bikeData.setChargingLvl(100);
        }else{
            bikeData.setChargingLvl(chargingLvl);
        }
        bikeData.setDateTime(LocalDateTime.now());
        bikeDAO.addBikeData(bikeData, tManager.getTripId());
    }

    private DockingStation findRandomStation() {
        ArrayList<DockingStation> stations = stationDAO.getAllDockingStations();

        int rNumber = rn.nextInt(stations.size());
        if(prevStation!=null) {
            //System.out.println("INNE I IF");
            while (stations.get(rNumber).getStationId() == prevStation.getStationId()) {
                System.out.println("INNE I WHILE");
                rNumber = rn.nextInt(stations.size());
            }
        }
        return stations.get(rNumber);
    }

    public static void main (String [] args) {
        BikeDAO bd = new BikeDAO();
        //TestTrip trip = new TestTrip(bd.getBike(1));

        //System.out.println(trip.bikeCycling());

    }
}
