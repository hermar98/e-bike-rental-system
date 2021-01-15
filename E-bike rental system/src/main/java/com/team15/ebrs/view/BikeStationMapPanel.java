package main.java.com.team15.ebrs.view;

import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.dao.DockingStationDAO;
import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.BikeData;
import main.java.com.team15.ebrs.data.DockingStation;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * BikeStationMapPanel is a container class with frame with the specified layout showing a map with positions of bikes and dockingstations
 * @author Team 15
 *
 */
public class BikeStationMapPanel extends JPanel{
    private JPanel topLine;
    private JPanel bottomLine;
    private JPanel infoBox;
    private JLabel infoBoxTitle;
    private JMapViewer mapViewer;
    private ArrayList<DockingStation> stationList;
    private ArrayList<Bike> bikeList;
    private ArrayList<BikeData> bikeDataList;
    private BikeDAO bikeDAO = new BikeDAO();
    private DockingStationDAO stationDAO = new DockingStationDAO();
    private Coordinate mapCenter = new Coordinate(63.416035, 10.414123);
    private JCheckBox disableBikes;

    private static final int ZOOM_LEVEL = 12;

    private boolean bikeInfoDisplayed = false;
    private int lastSelectedId;

    private JFrame mainFrame;

    /**
     *The frame to be added to the panel
     *
     * @param frame
     */
    public BikeStationMapPanel (JFrame frame) {
        mainFrame = frame;
        initialize();
    }

    /**
     *Initialize the contents of the frame.
     */
    public void initialize() {
        setLayout(new BorderLayout());

        stationList = stationDAO.getAllDockingStations();
        bikeList = bikeDAO.getAllBikes();



        mapViewer = new JMapViewer();
        displayDockingStations();
        displayBikes();
        mapViewer.setDisplayPosition(mapCenter, ZOOM_LEVEL);
        mapViewer.setDisplayPosition(mapCenter, ZOOM_LEVEL);
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mapViewerClicked(e);
            }
        });
        mapViewer.setBounds(0,40,960,440);
        add(mapViewer, BorderLayout.CENTER);

        // BOTTOM LINE
        bottomLine = new JPanel(new BorderLayout());
        String mapInfoString = " Hold down the right mouse button to move the map. "
                + "Click on a station or a bike to view more information.";
        JLabel mapInfo = new JLabel(mapInfoString);
        disableBikes = new JCheckBox("Hide bikes from map ");
        disableBikes.addItemListener(e -> disableBikesAction());
        bottomLine.add(mapInfo, BorderLayout.WEST);
        bottomLine.add(disableBikes, BorderLayout.EAST);
        bottomLine.setPreferredSize(new Dimension(960, 25));
        add(bottomLine, BorderLayout.SOUTH);

        // TOP LINE
        topLine = new JPanel(new FlowLayout());
        topLine.setPreferredSize(new Dimension(960, 40));
        topLine.setBackground(new Color(0, 147, 209));
        // TOP BUTTONS
        JButton backButton = new JButton("< Back");
        JButton refreshButton = new JButton("Refresh");
        // TOP BUTTON EVENTS
        backButton.addActionListener(e -> backButtonAction());
        refreshButton.addActionListener(e -> refreshButtonAction());
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel title = new JLabel("Bikes and Docking Stations Map", JLabel.CENTER);
        title.setPreferredSize(new Dimension(750, 35));
        title.setFont(new Font("Century Gothic Bold Italic", Font.PLAIN, 26));
        title.setForeground(Color.WHITE);
        topLine.add(backButton);
        topLine.add(title);
        topLine.add(refreshButton);
        add(topLine, BorderLayout.NORTH);
    }

    /**
     * a private method for setting the docking stations on the map according to coordinate system of them. 
     */
    private void displayDockingStations() {
        for (DockingStation ds : stationList) {
            Coordinate position = new Coordinate(ds.getCoordinateLat(), ds.getCoordinateLng());
            String dsName = ds.getStationName();
            MapMarkerDot mapMarker = new MapMarkerDot(dsName, position);
            //System.out.println("Name: " + dsName + " Coordinates: " + position);
            //System.out.println(mapViewer);
            mapViewer.addMapMarker(mapMarker);
        }
    }

    /**
     *a private method for setting the bikes on the map according to coordinate system of them. 
     */
    private void displayBikes() {
        for (Bike bike : bikeList) {
            BikeData data = bike.getBikeData();
            double lat = data.getLatitude();
            double lng = data.getLongitude();
            System.out.println();
            if(lat == 63.434903 && lng == 10.402521){ //Just temporary fix
                return;
            } if (bike.getStationId() == 0) {
                Coordinate position = new Coordinate(lat, lng);
                String label = "Bike " + bike.getBikeId();
                MapMarkerDot mapMarker = new MapMarkerDot(label, position);
                mapMarker.setBackColor(Color.GREEN);
                mapViewer.addMapMarker(mapMarker);
            }
        }
    }

    /**
     * The changes are occurred on the map when a mouse action occurred in a component
     * @param e - An event which indicates that a mouse action occurred in a component. 
     */
    private void mapViewerClicked (MouseEvent e) {
        if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
            Point click = e.getPoint();
            int clickX = click.x;
            int clickY = click.y;
            int radius = MapMarkerDot.DOT_RADIUS;
            List<MapMarker> markerList = mapViewer.getMapMarkerList();
            Iterator<MapMarker> i = markerList.iterator();
            while (i.hasNext()) {
                MapMarkerDot mapMarker = (MapMarkerDot) i.next();
                Coordinate markerPos = mapMarker.getCoordinate();
                Point marker = mapViewer.getMapPosition(markerPos);
                if (marker != null) {
                    int markerX = marker.x;
                    int markerY = marker.y;
                    boolean XValueOk = clickX > (markerX - radius) && clickX < (markerX + radius);
                    boolean YValueOk = clickY > (markerY - radius) && clickY < (markerY + radius);
                    if (XValueOk && YValueOk) {
                        mapViewer.setDisplayPosition(markerPos, ZOOM_LEVEL);
                        if (mapMarker.getBackColor() == Color.YELLOW) {
                            displayStationInfo(mapMarker.getName());
                        } else {
                            displayBikeInfo(mapMarker.getName());
                        }
                        revalidate();
                    }
                }
            }
        }
    }

    /**
     * The action to happen when the back button is clicked 
     */
    private void backButtonAction ()  {
        MainMenuPanel mainMenuScreen = new MainMenuPanel(mainFrame);
        mainFrame.setContentPane(mainMenuScreen);
        mainFrame.revalidate();
    }

    /**The action to happen when the refresh button is clicked 
     *
     */
    private void refreshButtonAction () {
        mapViewer.removeAllMapMarkers();
        stationList = stationDAO.getAllDockingStations();
        bikeDataList = bikeDAO.getAllTravellingData();
        bikeList = bikeDAO.getAllBikes();
        displayDockingStations();
        if (!disableBikes.isSelected()) {
            displayBikes();
        }
        if (infoBox != null) {
            if (bikeInfoDisplayed) {
                displayBikeInfo("Bike " + lastSelectedId);
            } else {
                DockingStation ds = stationDAO.getStationByID(lastSelectedId);
                displayStationInfo(ds.getStationName());
            }
            revalidate();
        }
    }

    /**
     * The action to happen when the check box is clicked
     */
    private void disableBikesAction () {
        if (disableBikes.isSelected()) {
            List<MapMarker> toRemove = new ArrayList<>();
            List<MapMarker> markerList = mapViewer.getMapMarkerList();
            for (MapMarker mm : markerList) {
                if (mm.getBackColor().equals(Color.GREEN)) {
                    toRemove.add(mm);
                }
            }
            for (MapMarker mm : toRemove) {
                mapViewer.removeMapMarker(mm);
            }
        } else {
            displayBikes();
        }
    }

    /**
     *The information of the specified station will be shown on the infoBox
     *
     * @param stationName - the name of the specified station on the map
     */
    private void displayStationInfo(String stationName) {
        if (infoBox != null) {
            remove(infoBox);
        }
        infoBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoBox.setPreferredSize(new Dimension(250, 540));
        infoBox.setBackground(new Color(224, 220, 219));
        DockingStation station = null;
        for (DockingStation ds : stationList) {
            if (ds.getStationName().equals(stationName)) {
                station = ds;
                break;
            }
        }
        infoBoxTitle = new JLabel(station.getStationName(), JLabel.CENTER);
        infoBoxTitle.setFont(new Font("Helvetica", Font.BOLD, 18));
        infoBoxTitle.setPreferredSize(new Dimension(250, 75));
        JLabel stationId = new JLabel(" Station ID: " + station.getStationId());
        stationId.setPreferredSize(new Dimension(250, 25));
        JLabel coords = new JLabel(" Coordinates: " + station.getCoordinateLat() + ", " + station.getCoordinateLng());
        coords.setPreferredSize(new Dimension(250, 25));
        JLabel totDocks = new JLabel(" Number of docks: " + station.getAntDocks());
        totDocks.setPreferredSize(new Dimension(250, 25));
        double powerUsage = stationDAO.getPowerUsage(station);
        JLabel powerUsageInfo = new JLabel(" Current power usage: " + powerUsage + " kW");
        powerUsageInfo.setPreferredSize(new Dimension(250, 25));

        infoBox.add(infoBoxTitle);
        infoBox.add(stationId);
        infoBox.add(coords);
        infoBox.add(totDocks);
        infoBox.add(powerUsageInfo);

        List<Bike> bikesAtStation = bikeDAO.getAllBikes(station.getStationId());
        if (!bikesAtStation.isEmpty()) {
            DefaultListModel listModel = new DefaultListModel();
            for (Bike bike : bikesAtStation) {
                listModel.addElement(bike);
            }
            JList bikeList = new JList(listModel);
            bikeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane bikeScrollList = new JScrollPane(bikeList);
            bikeScrollList.setPreferredSize(new Dimension(190, 150));

            JLabel listTitle = new JLabel(" Bikes currently at this station: ");
            listTitle.setPreferredSize(new Dimension(250, 25));

            infoBox.add(listTitle);
            infoBox.add(bikeScrollList);
        } else {
            JLabel noBikes = new JLabel(" No bikes are currently at this station. ");
            noBikes.setPreferredSize(new Dimension(250, 50));
            infoBox.add(noBikes);
        }

        JButton closeButton = new JButton("Hide info");
        closeButton.addActionListener(e -> removeInfoBox());
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


        infoBox.add(closeButton);
        add(infoBox, BorderLayout.EAST);
        bikeInfoDisplayed = false;
        lastSelectedId = station.getStationId();
    }

    /**
     * The information of the specified bike will be shown on the infoBox
     * @param bikeName the name of the specified bike on the map
     */
    private void displayBikeInfo(String bikeName) {
        if (infoBox != null) {
            remove(infoBox);
        }
        infoBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoBox.setPreferredSize(new Dimension(250, 540));
        infoBox.setBackground(new Color(224, 220, 219));
        Bike bike = null;
        BikeData bikeData = null;
        for (Bike b : bikeList) {
            int id = Integer.parseInt(bikeName.substring(5));
            if (id == b.getBikeId()) {
                bike = b;
                break;
            }
        }
        for (BikeData bd : bikeDataList) {
            if (bike.getBikeId() == bd.getBikeId()) {
                bikeData = bd;
            }
        }
        infoBoxTitle = new JLabel(bikeName, JLabel.CENTER);
        infoBoxTitle.setFont(new Font("Helvetica", Font.BOLD, 18));
        infoBoxTitle.setPreferredSize(new Dimension(250, 75));
        infoBox.add(infoBoxTitle);

        if (bikeData != null) {
            JLabel coords = new JLabel(" Coordinates: " + bikeData.getLatitude() + ", " + bikeData.getLongitude());
            coords.setPreferredSize(new Dimension(250, 25));
            JLabel chargingLvl = new JLabel(" Charging level: " + bikeData.getChargingLvl() + " %");
            chargingLvl.setPreferredSize(new Dimension(250, 25));
            infoBox.add(coords);
            infoBox.add(chargingLvl);
        } else {
            DockingStation station = stationDAO.getStationByID(bike.getStationId());
            JLabel atStation = new JLabel(" The bike is currently at " + station.getStationName() + ".");
            atStation.setPreferredSize(new Dimension(250, 25));
            infoBox.add(atStation);
        }

        JLabel make = new JLabel(" Make: " + bike.getMake());
        make.setPreferredSize(new Dimension(250, 25));
        JLabel type = new JLabel(" Type: " + bike.getType().getTypeName());
        type.setPreferredSize(new Dimension(250, 25));

        infoBox.add(make);
        infoBox.add(type);

        JButton closeButton = new JButton("Hide info");
        closeButton.addActionListener(e -> removeInfoBox());
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        infoBox.add(closeButton);

        add(infoBox, BorderLayout.EAST);
        bikeInfoDisplayed = true;
        lastSelectedId = bike.getBikeId();
    }

    /**
     * The info box will be unavailable on the panel
     */
    private void removeInfoBox () {
        remove(infoBox);
        infoBox = null;
        revalidate();
    }

    /**
     * launch the window
     * @param args
     */
    public static void main (String [] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    BikeStationMapPanel mapScreen = new BikeStationMapPanel(frame);
                    frame.setContentPane(mapScreen);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
