
package main.java.com.team15.ebrs.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.dao.DockingStationDAO;
import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.DockingStation;
import main.java.com.team15.ebrs.data.DockingStationData;

/**
 * Class used for updating power usage for the docking stations. Uses a default value for the power consumption of each bike.
 * @author Team 15.
 */
public class StationManager{
    private static DockingStation dock;
    private static final DockingStationDAO dDAO = new DockingStationDAO();
    private static final BikeDAO bDAO = new BikeDAO();
    private static final double DEFAULT_USAGE_PER_BIKE = 20;
    private static ArrayList<Bike> bikesAtStation;
    
    /**
     * Updates the power usage into the database. Checks how many bikes are at the station and not fully charged.
     * @param dock The specified docking station you want to update power usage at.
     */
    public static void UpdatePowerUsage(DockingStation dock){
        bikesAtStation = bDAO.getAllBikes(dock.getStationId());
        int chargingBikes = 0;
        
        for(Bike b : bikesAtStation){
            if(b.getBikeData().getChargingLvl() < 100){
                chargingBikes++;
            }
        }
        
        double powerUsage = chargingBikes * DEFAULT_USAGE_PER_BIKE;
        DockingStationData dsd = new DockingStationData(dock.getStationId(), LocalDateTime.now(), powerUsage);
        dDAO.addDockingStationData(dsd);
    }
}