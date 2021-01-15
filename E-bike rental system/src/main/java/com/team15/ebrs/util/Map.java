package main.java.com.team15.ebrs.util;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import main.java.com.team15.ebrs.data.BikeData;
import main.java.com.team15.ebrs.data.DockingStation;

/**
 * Class used to display docking stations and bikes in the map in the application.
 * @author Team 15
 */
public class Map {
    /**
     * Returns JMapViewer object displaying the position of a bike using the BikeData object.
     * @param bikedata BikeData object containing the coordinates for the bike.
     * @return JMapViewer object with the bikes position.
     */
    public static JMapViewer getBikeMap(BikeData bikedata) {
        double lat = bikedata.getLatitude();
        double lng = bikedata.getLongitude();
        String label = "Bike " + bikedata.getBikeId();

        JMapViewer mapViewer = new JMapViewer();
        Coordinate bikePos = new Coordinate(lat, lng);
        mapViewer.setDisplayPosition(bikePos, 12);
        mapViewer.addMapMarker(new MapMarkerDot(label, bikePos));
        return mapViewer;
    }

    /**
     * Returns a JMapViewer displaying a selected docking station at a map. 
     * @param dockingstation The docking station that should be displayed.
     * @return JMapViewer object displaying specified docking station.
     */
    public static JMapViewer getStationMap(DockingStation dockingstation) {
        double lat = dockingstation.getCoordinateLat();
        double lng = dockingstation.getCoordinateLng();
        String label = dockingstation.getStationName();

        JMapViewer mapViewer = new JMapViewer();
        Coordinate stationPos = new Coordinate(lat, lng);
        mapViewer.setDisplayPosition(stationPos, 12);
        mapViewer.addMapMarker(new MapMarkerDot(label, stationPos));
        return mapViewer;
    }
}
