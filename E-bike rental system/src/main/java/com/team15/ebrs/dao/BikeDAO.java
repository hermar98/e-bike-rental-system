
package main.java.com.team15.ebrs.dao;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.Calendar;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.SimpleDateFormat;

import static java.sql.Types.INTEGER;
import java.time.LocalDate;
import java.time.LocalDateTime;

import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.BikeData;
import main.java.com.team15.ebrs.data.DockingStation;
import main.java.com.team15.ebrs.data.BikeType;
import main.java.com.team15.ebrs.util.DatabaseConnector;

/**
 * Data Access Object class for Bike and BikeData. Provides necessary database interaction regarding the specified objects.
 * @author Team 15
 * @see Bike
 * @see BikeData
 * 
 */
public class BikeDAO {
    private ArrayList<Bike> bikelist = new ArrayList<>();
    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    //Needed SQL querys
    private final String BIKE_ID_TYPE = "SELECT MAX(bike_id) AS bike_id FROM bike NATURAL JOIN bike_type WHERE type_id = ";
    private final String BIKE_BY_ID = "SELECT * FROM active_bikes NATURAL JOIN recent_bike_data WHERE bike_id = ";
    private final String INSERT = "INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id,active) VALUES (?,?,?,?,?,?,DEFAULT)";
    private final String ALL_BIKES = "SELECT * FROM active_bikes NATURAL JOIN recent_bike_data GROUP BY bike_id";
    private final String BIKES_AT_STATION = "SELECT * FROM active_bikes NATURAL JOIN recent_bike_data Where station_id = ";
    private final String EDIT = "UPDATE bike SET purchase_date = ?, price = ?, make = ?, type_id = ?, station_id = ? WHERE bike_id = ? ";
    private final String DELETE = "UPDATE bike SET active = ? WHERE bike_id = ?";
    private final String BIKE_DATA_1 = "SELECT * FROM bike_data WHERE bike_id = ";
    private final String BIKE_DATA_2 = " ORDER BY bike_data.date_time DESC";
    private final String INSERT_DATA = "INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl, trip_id) VALUES (?,?,?,?,?,?)";
    private final String DELETE_DATA = "DELETE FROM bike_data WHERE bike_id = ?";
    private final String RECENT_BIKE_DATA = "SELECT * FROM recent_bike_data WHERE bike_id = ";
    private final String ALL_RECENT_DATA = "SELECT * FROM recent_bike_data";
    private final String ALL_TRAVEL_DATA = "SELECT * FROM travelling_bikes";
    private final String RANDOM_AVAILABLE_ID = "SELECT available_bikes.bike_id FROM available_bikes";
    private final String ALL_AVAILABLE_BIKES = "SELECT * FROM available_bikes NATURAL JOIN recent_bike_data";
    private final String NUM_BIKES_AT_STATION = "SELECT * FROM active_bikes WHERE station_id = ";
    
    //Coloumn labels in needed tables from database
    private final String cBikeID = "bike_id";
    private final String cDate = "purchase_date";
    private final String cPrice = "price";
    private final String cMake = "make";
    private final String cTypeId = "type_id";
    private final String cTypeName = "type_name";
    private final String cRentalPrice = "rental_price";
    private final String cStationId = "station_id";
    private final String cDateTime = "date_time";
    private final String cLatitude = "coords_lat";
    private final String cLongitude = "coords_lng";
    private final String cChargingLevel = "charging_lvl";

    /**
     * Returns an ArrayList of all bikes. Bikes previously deleted but still in the system will not be included.
     *
     * @return ArrayList with Bike objects
     */
    public ArrayList<Bike> getAllBikes (){
        bikelist = new ArrayList<>();
        
        try(
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(ALL_BIKES);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
            
            	Bike bike = getBikeFromRS(rs);
                bikelist.add(bike);
            }
            
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
    return bikelist;
    }

    /**
     * Fetches all bikes registered at the specified docking station.
     * @param dockingStationId Specify which dockingstation you want to get all bikes from.
     * @return an ArrayList of Bikes at the selected docking station.
     */
    public ArrayList<Bike> getAllBikes(int dockingStationId){ //All bikes currently in the selected docking station
        ArrayList<Bike> BList = new ArrayList<>();

        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(BIKES_AT_STATION + dockingStationId);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                Bike bike = getBikeFromRS(rs);
                BList.add(bike);
            }
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
        return BList;
    }

    /**
     * Returns all Bikes registered to the system that is available, meaning not having any unfinished repairs nor having stationId set to null.
     * @return ArrayList with available Bikes.
     */
    public ArrayList<Bike> getAllAvailableBikes() {
        bikelist = new ArrayList<>();
        
        try(Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(ALL_AVAILABLE_BIKES);
             ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                Bike bike = getBikeFromRS(rs);
                bikelist.add(bike);
            }
            
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
        return bikelist;
    }

    /**
     * Finds the most recent BikeData for all active bikes in the system.
     * @return ArrayList of BikeData.
     */
    public ArrayList<BikeData> getAllRecentData (){
        ArrayList<BikeData> datalist = new ArrayList<>();
        
        try(
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(ALL_RECENT_DATA);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                int bikeId = rs.getInt(cBikeID);
                LocalDateTime dateTime = rs.getTimestamp(cDateTime).toLocalDateTime();
                double latitude = rs.getDouble(cLatitude);
                double longitude = rs.getDouble(cLongitude);
                double chargingLevel = rs.getDouble(cChargingLevel);

                BikeData bd = new BikeData(bikeId, dateTime, latitude, longitude, chargingLevel);
                datalist.add(bd);
            }
            
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
    return datalist;
    }

    /**
     * Returns all BikeData registered for the specified Bike.
     * @param bike The Bike you want to retrieve all BikeData about.
     * @return ArrayList of BikeData.
     */
    public ArrayList<BikeData> getAllBikeData (Bike bike){
    ArrayList<BikeData> datalist = new ArrayList<>();
        
        try(
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(BIKE_DATA_1 + bike.getBikeId() + BIKE_DATA_2);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                int bikeId = rs.getInt(cBikeID);
                LocalDateTime dateTime = rs.getTimestamp(cDateTime).toLocalDateTime();
                double latitude = rs.getDouble(cLatitude);
                double longitude = rs.getDouble(cLongitude);
                double chargingLevel = rs.getDouble(cChargingLevel);
                
                BikeData bd = new BikeData(bikeId, dateTime, latitude, longitude, chargingLevel);
                datalist.add(bd);
            }
            
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
    return datalist;
    }

    /**
     * Returns recent BikeData for all Bikes currently travelling.
     * @return ArrayList of BikeData.
     */
    public ArrayList<BikeData> getAllTravellingData (){
    ArrayList<BikeData> datalist = new ArrayList<>();
        
        try(
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(ALL_TRAVEL_DATA);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                int bikeId = rs.getInt(cBikeID);
                LocalDateTime dateTime = rs.getTimestamp(cDateTime).toLocalDateTime();
                double latitude = rs.getDouble(cLatitude);
                double longitude = rs.getDouble(cLongitude);
                double chargingLevel = rs.getDouble(cChargingLevel);
                
                BikeData bd = new BikeData(bikeId, dateTime, latitude, longitude, chargingLevel);
                datalist.add(bd);
            }
            
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
    return datalist;
    }

    /**
     * Edits a Bike registered in the database matching the specified bikeId.
     * @param bike The edited version of the bike you want in the database.
     * @return Boolean stating whether the editing of the bike in the system was successful.
     */
    public boolean editBike(Bike bike){//Edits the desired bike with matching bikeId
        if(!findBike(bike.getBikeId())){
            return false;
            //Bike_ID not registered in database, can't edit what's not there
        }else{
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(EDIT)){
                
                ps.setDate(1, bike.getSqlDate());
                ps.setDouble(2, bike.getPrice());
                ps.setString(3, bike.getMake());
                ps.setInt(4,bike.getTypeId());
                if(bike.getStationId() == 0){
                    ps.setNull(5, INTEGER);
                }else{
                    ps.setInt(5, bike.getStationId());
                }
                ps.setInt(6, bike.getBikeId());
                ps.executeUpdate();
                return true;
            }catch(Exception e){
                System.out.println("Exception: " + e);
            }
        }
        return false;
    }

    /**
     * Sets active BIT to FALSE (0) in the database for the specified Bike, which hides it from the application. No data will be deleted. The deletion checks for matching bikeId in the database.
     * @param bike The specified Bike you want deleted.
     * @return Int stating whether the deletion of the bike in the system was successful. 0 - deleted, 1 - not deleted, 2 - not found.
     */
    public int deleteBike(Bike bike){
        final int DELETED = 0;
        final int NOT_DELETED = 1;
        final int NOT_FOUND = 2;
        final boolean NOT_ACTIVE = false;
        
        if(!findBike(bike.getBikeId())){//BikeId not in database
            return NOT_FOUND; //
        }else{
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE)){
                
                ps.setBoolean(1, NOT_ACTIVE);
                ps.setInt(2, bike.getBikeId());
                ps.executeUpdate();
                return DELETED;
            }catch(Exception e){
                System.out.println("Exception: " + e);
            }
        }
        return NOT_DELETED;
    }

    /**
     * Adds a bicycle to the database. If no DockingStation is specified, the method will check for BikeData within the Bike.
     * If there's no BikeData, the "Company's" standard coordinates will be used.
     * 
     * @param bike The Bike object being added to the database. Any bikeId set will be ignored as this is decided by the existing bikes already registered.
     * @param dock The DockingStation the bike is added to. Null if the bike shouldn't be set to a DockingStation.
     * @return boolean stating whether the registration to the system was successful.
     */
    public boolean addBike(Bike bike, DockingStation dock){
        String dateTimeNow = new SimpleDateFormat(TIME_FORMAT).format(Calendar.getInstance().getTime());
    	int bikeId = 1;
    	double latitude;
    	double longitude;
        double chargingLvl;
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(INSERT);
            PreparedStatement ps2 = con.prepareStatement(INSERT_DATA);
            PreparedStatement ps3 = con.prepareStatement(BIKE_ID_TYPE + bike.getTypeId());
            ResultSet rs = ps3.executeQuery()){
            
            con.setAutoCommit(false);
            
            if(rs.next()){
                int maxBikeId = rs.getInt(cBikeID);
                if(maxBikeId == 0){
                    bikeId = bike.getTypeId() * 1000;
                }else{
                    bikeId += maxBikeId;
                }
            }else{
                return false;
            }

            ps.setInt(1, bikeId);
            ps.setDate(2, bike.getSqlDate());
            ps.setDouble(3, bike.getPrice());
            ps.setString(4, bike.getMake());
            ps.setInt(5, bike.getTypeId());
            if(bike.getStationId() == 0){
                ps.setNull(6, INTEGER);
            }else{
                ps.setInt(6, bike.getStationId());
            }
            
            if (dock != null){
                latitude = dock.getCoordinateLat();
                longitude = dock.getCoordinateLng();
                chargingLvl = 100;
                ps.setInt(6, dock.getStationId());
            }else if(bike.getBikeData() != null){
                latitude = bike.getBikeData().getLatitude();
                longitude = bike.getBikeData().getLongitude();
                chargingLvl = bike.getBikeData().getChargingLvl();
            }else{ //If there's no selected dock or data input for the bike
                latitude = 63.434903; // The latitude & longitude of the "company"
                longitude = 10.402521;
                chargingLvl = 100;
            }
            ps2.setInt(1, bikeId);
            ps2.setString(2, dateTimeNow);
            ps2.setDouble(3,latitude);
            ps2.setDouble(4, longitude);
            ps2.setDouble(5, chargingLvl);
            ps2.setNull(6, INTEGER);

            ps.executeUpdate();
            ps2.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            return true;
            }catch(Exception e){
                System.out.println("Exception: " + e);
            }
        return false;
    }

    /**
     * Mostly used as a help method in various methods to quick check if the specified bikeId is registered in the database
     * @param bikeId The specified bikeId you want to find.
     * @return Boolean stating whether the Bike was found in the system.
     */
    public boolean findBike(int bikeId){
        
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(BIKE_BY_ID + bikeId);
            ResultSet rs = ps.executeQuery()){
            
            if(rs.isBeforeFirst()){
                return true; //Bike_id found in database
            }
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }    
        return false;
    }

    /**
     * Returns a Bike matching the bikeId specified. Null if the bikeId is not found.
     * @param bikeId The bikeId of the wanted Bike object.
     * @return The specifed Bike object.
     */
    public Bike getBike(int bikeId){
        
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(BIKE_BY_ID + bikeId);
            ResultSet rs = ps.executeQuery()){
            
            if(rs.next()){
                Bike bike = getBikeFromRS(rs);
                return bike;
            }
        }catch(Exception e){
            System.out.println("Exception" + e);
        }
        return null;
    }

    /**
     * Help method used in varous methods in BikeDAO. Fetches a Bike object from the specified ResultSet.
     * @param rs ResultSet containing enough columns to create a Bike object.
     * @return A Bike object. Null if no info is found.
     */
    private Bike getBikeFromRS(ResultSet rs){ //Help method to get a bike from a ResultSet query
        try{
            int bID = rs.getInt(cBikeID);
            LocalDate date = rs.getDate(cDate).toLocalDate();
            Double price = rs.getDouble(cPrice);
            String make = rs.getString(cMake);
            int typeId = rs.getInt(cTypeId);
            String typeName = rs.getString(cTypeName);
            double rentalPrice = rs.getDouble(cRentalPrice);
            int stationId = rs.getInt(cStationId);
            LocalDateTime dateTime = rs.getTimestamp(cDateTime).toLocalDateTime();
            double lat = rs.getDouble(cLatitude);
            double lng = rs.getDouble(cLongitude);
            double chargingLvl = rs.getDouble(cChargingLevel);

            BikeData bd = new BikeData(bID, dateTime, lat, lng, chargingLvl);
            BikeType t = new BikeType(typeName,rentalPrice);
            t.setTypeId(typeId);
            Bike bike = new Bike(date, price, make, t, stationId);
            bike.setBikeId(bID);
            bike.setBikeData(bd);
            return bike;
        }catch(Exception e){
            System.out.println("Exception " + e);
        }
        return null;
    }

    /**
     * Help method used in varous methods in BikeDAO. Fetches a BikeData object from the specified ResultSet.
     * @param rs ResultSet containing enough columns to create a BikeData object.
     * @return A BikeData object. Null if no info is found.
     */
    public BikeData getBikeDataFromRS(ResultSet rs){
        try{
            int bikeId = rs.getInt(cBikeID);
            LocalDateTime dateTime = rs.getTimestamp(cDateTime).toLocalDateTime();
            double latitude = rs.getDouble(cLatitude);
            double longitude = rs.getDouble(cLongitude);
            double chargingLevel = rs.getDouble(cChargingLevel);
            BikeData bd = new BikeData(bikeId, dateTime, latitude, longitude, chargingLevel);
            return bd;
        }catch(Exception e){
            System.out.println("Exception " + e);
        }
        return null;
    }
    
    /**
     * Adds BikeData to the system. If tripId = 0 then no trip will be connected to this BikeData
     * @param bd BikeData object that is being added to the database.
     * @param tripId tripId, 0 if no trip is connected.
     * @return Boolean stating whether the BikeData was stored successfully.
     */
    public boolean addBikeData(BikeData bd, int tripId){
        int CHARGING_LEVEL_0 = 0;
    	int bikeId = bd.getBikeId();
    	try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(INSERT_DATA)){
            
            ps.setInt(1, bikeId);
            ps.setTimestamp(2, bd.getSqlDateTime());
            ps.setDouble(3,bd.getLatitude());
            ps.setDouble(4, bd.getLongitude());
            
            if(bd.getChargingLvl() > 0){
                ps.setDouble(5, bd.getChargingLvl());
            }else{
                ps.setDouble(5, CHARGING_LEVEL_0);
            }
            if(tripId == 0){
                ps.setNull(6, INTEGER);              
            }else{
                ps.setInt(6, tripId);
            }
            System.out.println("SETTING TRIP ID" + tripId + "BIKE ID : " + bikeId);
            ps.executeUpdate();
            return true;
            
        }catch(Exception e) {
        System.out.println("Exception at addBikeData " + e);
        }
        return false;
     }

    /**
     * Returns the newest BikeData registered for the specified Bike.
     * @param bike The desired Bike you will retrieve BikeData from.
     * @return A BikeData object with the newest data for the Bike.
     */
    public BikeData getRecentData(Bike bike){ //Returns a BikeData object with the most recent data for the specified bike

        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(RECENT_BIKE_DATA + bike.getBikeId());
            ResultSet rs = ps.executeQuery()){
            
            if(rs.next()){
                BikeData bd = getBikeDataFromRS(rs);
                return bd;
            }
            
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
        return null;
    }
    /**
     * Method used for simulation. Stations need to know when all bikes are finished.
     * @return Number of bikes currently travelling.
     */
    public int getNumOfTravellingBikes(){
        int numberOfBikes = 0;
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(ALL_TRAVEL_DATA);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                numberOfBikes++;
            }
            
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
        return numberOfBikes;
    }
    
    public int getNumOfBikes(DockingStation dock){
        int numberOfBikes = 0;
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(NUM_BIKES_AT_STATION + dock.getStationId());
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                numberOfBikes++;
            }
            
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
        return numberOfBikes;
    }

    /**
     * Delete all BikeData registered at the selected bike.
     * @param bike The Bike you want all data removed from.
     * @return Boolean stating whether the deletion was successful.
     */
    private boolean deleteAllData(Bike bike){
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE_DATA)){
                
                ps.setInt(1, bike.getBikeId());
                ps.executeUpdate();
                return true;
            }catch(Exception e){
                System.out.println("Exception: " + e);
            }
            return false;
    }
}
