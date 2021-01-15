package main.java.com.team15.ebrs.view;

import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.dao.DockingStationDAO;
import main.java.com.team15.ebrs.dao.TypeDAO;
import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.BikeType;
import main.java.com.team15.ebrs.data.DockingStation;
import main.java.com.team15.ebrs.util.Calculations;
import main.java.com.team15.ebrs.util.Charts;
import main.java.com.team15.ebrs.util.TransparentListCellRenderer;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * StatsPanel is a container class with frame with the specified layout for the statistics
 * @author Team 15
 *
 */

public class StatsPanel extends JPanel {

    private final String BIKE_DISTANCE_CHART ="Bike Total Distance";
    private final String BIKE_TRAFFIC_PER_STATION ="Bikes checked out/in of stations";
    private final String PROFIT_PER_MONTH = "Profit each month";
    private static final String BG_IMAGE_URL = "/main/resources/com/team15/ebrs/images/statisticsNew.png";



    private JButton backButton;
    private JTextPane textPane;
    private JPanel chartPanel;
    private JPanel dynamicPanel;
    private JComboBox<String> comboBox;
    private JLabel background;
    private JScrollPane textScrollPane;

    private JFrame mainFrame;

    /**
     * Constructor to build the panel into the frame. Here you choose the frame you want to display the panel in.
     * @param frame This parameter says which frame to put the panel in.
     */
    public StatsPanel(JFrame frame) {
        mainFrame = frame;
        initialize();
	}

    /**
     * Initialize the contents of the panel.
     */
    public void initialize() {
        setLayout(null);
        // BUTTONS
        backButton = new JButton("");
        // BUTTON SIZES
        backButton.setBounds(26,8,75,75);
        // BUTTON FONT
        backButton.setFont(new Font("Verdana",Font.BOLD,16));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // BUTTON EVENTS
        backButton.addActionListener(e -> backButtonAction());
        //TEXT PANE
        textPane = new JTextPane();
        //textPane.setBackground(Color.PINK);
        textPane.setText("This is a text area.");
        textPane.setEditable(false);
        textScrollPane = new JScrollPane(textPane);
        textScrollPane.setBounds(66,206,300,212);
        textScrollPane.setBorder(null);
       // textScrollPane.getVerticalScrollBar().setValue(0);
        //CHART PANEL
        chartPanel = new JPanel();
        chartPanel.setBounds(380,60, 530,370 );
        //chartPanel.setBackground(Color.GREEN);
        chartPanel.setOpaque(false);
        updateChartsEvent(BIKE_DISTANCE_CHART);
        //COMBOBOX
        comboBox = new JComboBox();
        comboBox.setBounds(93 ,98,245,35);
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        comboBox.addItem(BIKE_DISTANCE_CHART);
        comboBox.addItem(BIKE_TRAFFIC_PER_STATION);
        comboBox.addItem(PROFIT_PER_MONTH);
        //comboBox.setBackground(Color.decode("#104580"));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(Color.BLACK);
        comboBox.setBorder(null);
        //comboBox.setOpaque(false);
        //comboBox.setRenderer(new TransparentListCellRenderer());

        comboBox.addActionListener(e -> comboBoxEvent(e));

        add(backButton);
        add(textScrollPane);
        add(chartPanel);
        add(comboBox);
        /* Load the background */
        URL bgUrl = getClass().getResource(BG_IMAGE_URL);
        ImageIcon bgImage = new ImageIcon(bgUrl);

        background = new JLabel(bgImage);
        background.setBounds(0, -10, 960, 540);
        add(background);
    }

    /**
     *  This is a private method that gets called by the back button.
     */
    private void backButtonAction () {
        MainMenuPanel mainMenuScreen = new MainMenuPanel(mainFrame);
        mainFrame.setContentPane(mainMenuScreen);
        mainFrame.revalidate();
    }

    /**
     *  This is a private method that gets called by the combo box.
     * @param e This parameter is the comboBox ActionEvent
     */
    private void comboBoxEvent (ActionEvent e) {
        JComboBox<String> cbox = (JComboBox<String>) e.getSource();
        String selected = (String) cbox.getSelectedItem();
        updateChartsEvent(selected);
    }

    /**
     *  This is a private method that updates the chart and takes a parameter to choose what chart you want next.
     *
     * @param selected This parameter chooses which chart to display.
     */
    private void updateChartsEvent (String selected) {
        chartPanel.removeAll();

        ArrayList<String> names = new ArrayList<>();
        ArrayList<Double> numbers = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        Calculations cal = new Calculations();
        ChartPanel cp = null;

        String TITLE;
        String VALUES;
        String CATEGORIES;
        NumberFormat nF = new DecimalFormat("#0.000");
        textPane.setFont(new Font("Verdana",Font.BOLD,12));



        switch (selected){
            case BIKE_DISTANCE_CHART:
                TITLE = "Distance per bike";
                VALUES = "Distance (KM)";
                CATEGORIES = "Bikes";

                BikeDAO bd = new BikeDAO();
                ArrayList<Bike> bikes = bd.getAllBikes();
                double sumDis = 0;
                double lDis = -1;
                int lDisBike = -1;
                double sDis = 1000000; // Hopefully the total distance for a bike won't exceed 1 000 000 KM
                int sDisBike = -1;
                for(int i = 0; i < bikes.size(); i++){
                    double tDis = cal.getTotalDistanceByID(bikes.get(i).getBikeId())/1000;
                    names.add(""+bikes.get(i).getBikeId());
                    numbers.add(tDis);
                    labels.add("Bikes");
                    sumDis += tDis;
                    if(lDis<tDis){
                        lDis=tDis;
                        lDisBike = bikes.get(i).getBikeId();
                    }
                    if(sDis>tDis){
                        sDis=tDis;
                        sDisBike = bikes.get(i).getBikeId();
                    }
                }
                // AVERAGE DISTANCE
                double aveDis = sumDis/numbers.size();
                // MOST BIKES OF TYPE AND LEAST BIKES OF TYPE
                TypeDAO tyd = new TypeDAO();
                ArrayList<BikeType> types = tyd.getAllTypes();
                int[] typeCount = new int[types.size()];
                for(Bike aBike: bikes) {
                    for (int i = 0; i < types.size(); i++) {
                        if(types.get(i).getTypeId() == aBike.getTypeId()){
                            typeCount[i]++;
                        }
                    }
                }
                String mPTypeName = "404";
                int mPBikeAmount = -1;
                String lPTypeName = "404";
                int lPBikeAmount = 1000000; // Hopefully the amount of bikes won't exceed 1 000 000.
                for(int i = 0; i < types.size(); i++){
                    if(typeCount[i]> mPBikeAmount) {
                        mPTypeName = types.get(i).getTypeName();
                        mPBikeAmount = typeCount[i];
                    }
                    if(typeCount[i]< lPBikeAmount){
                        lPTypeName = types.get(i).getTypeName();
                        lPBikeAmount = typeCount[i];
                    }
                }


                cp = new ChartPanel(Charts.createBarChart(TITLE,VALUES,CATEGORIES,Charts.createBarDataset(numbers,names,labels)));

                // SET STATS TEXT
                textPane.setText(
                        "Longest Distance:\n    " + lDisBike + ", " + nF.format(lDis) + " KM\n" +
                        "Shortest Distance:\n    " + sDisBike + ", " + nF.format(sDis) + " KM\n\n" +

                         "Average Distance All Bikes: " + nF.format(aveDis) + " KM\n" +
                         "Total Distance All Bikes: " + nF.format(sumDis) + " KM\n\n" +

                         "Most Bikes Of Type:\n  " + mPTypeName + ", " + mPBikeAmount + " bikes\n" +
                         "Least Bikes Of Type:\n  " + lPTypeName + ", " + lPBikeAmount + " bikes\n"
                );
                textPane.setCaretPosition(0);
                break;
            case BIKE_TRAFFIC_PER_STATION:
                TITLE = "Bicycle traffic for each station";
                VALUES = "Number of bicycles";
                CATEGORIES = "";
                String label1 = "CHECK OUTS";
                String label2 = "CHECK INS";

                DockingStationDAO dd = new DockingStationDAO();
                ArrayList<DockingStation> stations = dd.getAllDockingStations();


                ArrayList<DefaultCategoryDataset> datasets = new ArrayList<>();

                //Creating the first part of dataset
                // MOST POPULAR
                ArrayList<String> mPCheckOutsNames = new ArrayList<>();
                mPCheckOutsNames.add("404");
                ArrayList<Double> mPCheckOuts = new ArrayList<>();
                mPCheckOuts.add(new Double(-1));
                // LEAST POPULAR
                ArrayList<String> lPCheckOutsNames = new ArrayList<>();
                lPCheckOutsNames.add("404");
                ArrayList<Double> lPCheckOuts = new ArrayList<>();
                lPCheckOuts.add(new Double(1000000));
                for(int i = 0; i < stations.size(); i++){
                    double totalCheckOuts = cal.getBikeCheckInsAndOuts(stations.get(i).getStationId(),false);
                    String stationName = stations.get(i).getStationName();
                    names.add(stationName);
                    numbers.add(totalCheckOuts);
                    labels.add(label1);
                    if(totalCheckOuts > mPCheckOuts.get(0)){
                        mPCheckOutsNames = new ArrayList<>();
                        mPCheckOuts = new ArrayList<>();
                        mPCheckOutsNames.add(stationName);
                        mPCheckOuts.add(totalCheckOuts);
                    } else if (totalCheckOuts == mPCheckOuts.get(0)) {
                        mPCheckOutsNames.add(stationName);
                        mPCheckOuts.add(totalCheckOuts);
                    }
                    if(totalCheckOuts < lPCheckOuts.get(0)){
                        lPCheckOutsNames = new ArrayList<>();
                        lPCheckOuts = new ArrayList<>();
                        lPCheckOutsNames.add(stationName);
                        lPCheckOuts.add(totalCheckOuts);
                    } else if (totalCheckOuts == lPCheckOuts.get(0)) {
                        lPCheckOutsNames.add(stationName);
                        lPCheckOuts.add(totalCheckOuts);
                    }
                }
                datasets.add(Charts.createBarDataset(numbers,names,labels));
                names = new ArrayList<>();
                numbers = new ArrayList<>();
                labels = new ArrayList<>();

                // MOST POPULAR
                ArrayList<String> mPCheckInsNames = new ArrayList<>();
                mPCheckInsNames.add("404");
                ArrayList<Double> mPCheckIns = new ArrayList<>();
                mPCheckIns.add(new Double(-1));
                // LEAST POPULAR
                ArrayList<String> lPCheckInsNames = new ArrayList<>();
                lPCheckInsNames.add("404");
                ArrayList<Double> lPCheckIns = new ArrayList<>();
                lPCheckIns.add(new Double(1000000));
                //Creating the second part of dataset

                for(int i = 0; i < stations.size(); i++){
                    double totalCheckIns = cal.getBikeCheckInsAndOuts(stations.get(i).getStationId(),true);
                    String stationName = stations.get(i).getStationName();
                    names.add(stationName);
                    numbers.add(totalCheckIns);
                    labels.add(label2);
                    if(totalCheckIns > mPCheckIns.get(0)){
                        mPCheckInsNames = new ArrayList<>();
                        mPCheckIns = new ArrayList<>();
                        mPCheckInsNames.add(stationName);
                        mPCheckIns.add(totalCheckIns);
                    } else if (totalCheckIns == mPCheckIns.get(0)) {
                        mPCheckInsNames.add(stationName);
                        mPCheckIns.add(totalCheckIns);
                    }
                    if(totalCheckIns < lPCheckIns.get(0)){
                        lPCheckInsNames = new ArrayList<>();
                        lPCheckIns = new ArrayList<>();
                        lPCheckInsNames.add(stationName);
                        lPCheckIns.add(totalCheckIns);
                    } else if (totalCheckIns == lPCheckIns.get(0)) {
                        lPCheckInsNames.add(stationName);
                        lPCheckIns.add(totalCheckIns);
                    }
                }
                datasets.add(Charts.createBarDataset(numbers,names,labels));

                cp = new ChartPanel(Charts.createBarChart(TITLE,VALUES,CATEGORIES,datasets));
                // SET STATS TEXT
                String mPCheckOutsName = "";
                for (int i = 0; i<mPCheckOutsNames.size(); i++) {
                    mPCheckOutsName += "     " + mPCheckOutsNames.get(i) + ", " + mPCheckOuts.get(i).intValue() + " check outs \n";
                }
                String mPCheckInsName = "";
                for (int i = 0; i<mPCheckInsNames.size(); i++) {
                    mPCheckInsName += "     " + mPCheckInsNames.get(i) + ", " + mPCheckIns.get(i).intValue() + " check ins \n";
                }
                String lPCheckOutsName = "";
                for (int i = 0; i<lPCheckOutsNames.size(); i++) {
                    lPCheckOutsName += "     " + lPCheckOutsNames.get(i) + ", " + lPCheckOuts.get(i).intValue() + " check outs \n";
                }
                String lPCheckInsName = "";
                for (int i = 0; i<lPCheckInsNames.size(); i++) {
                    lPCheckInsName += "     " + lPCheckInsNames.get(i) + ", " + lPCheckIns.get(i).intValue() + " check ins \n";
                }
                textPane.setText(
                        "Most Popular Stations Check Outs:\n" + mPCheckOutsName + "\n" +
                        "Most Popular Stations Check Ins: \n" + mPCheckInsName  + "\n" +

                        "Least Popular Stations Check Outs:\n" + lPCheckOutsName  + "\n" +
                        "Least Popular Stations Check Ins: \n" + lPCheckInsName  + "\n"
                );
                textPane.setCaretPosition(0);
                break;
            case PROFIT_PER_MONTH:
                TITLE = "Profit each month";
                VALUES = "Kroners (NOK)";
                CATEGORIES = "Month";
                int thisMonthInt = LocalDate.now().getMonthValue();
                int thisYearInt = LocalDate.now().getYear();

                int lastYearInt = thisYearInt-1;

                int targetMonthInt = thisMonthInt+1;
                int targetYearInt = lastYearInt;
                double totalProfit = 0;
                double mProfit = -1;
                String mProfitMonth = LocalDate.of(1,1,1).getMonth().toString();
                double lProfit = 1000000;
                String lProfitMonth = LocalDate.of(1,1,1).getMonth().toString();
                for(int i = 0; i < 12; i++){
                    double number;
                    if(targetMonthInt<13) {
                        targetYearInt=thisYearInt;
                        number =cal.getProfitForMonth(targetYearInt,targetMonthInt-12+i);
                        numbers.add(number);
                        totalProfit+=number;

                    } else {
                        number =cal.getProfitForMonth(targetYearInt,targetMonthInt+i);
                        numbers.add(number);
                        totalProfit+=number;
                    }
                    names.add("PROFIT");
                    int mLabel;
                    if(targetMonthInt+i<=12) {
                        //System.out.println("test1: " + (targetMonthInt+i));
                        mLabel =(targetMonthInt+i);
                        labels.add(""+mLabel);//LocalDate.of(targetYearInt, targetMonthInt+i, 1).getMonth().toString());
                    } else {
                        //System.out.println("test2: " + (targetMonthInt+i-12));
                        mLabel = (targetMonthInt+i-12);
                        labels.add(""+mLabel);//LocalDate.of(targetYearInt, targetMonthInt+i-12, 1).getMonth().toString());
                    }
                    if(mProfit<number){
                        mProfit=number;
                        mProfitMonth = LocalDate.of(1,mLabel,1).getMonth().toString();
                    }
                    if(lProfit>number){
                        lProfit=number;
                        lProfitMonth = LocalDate.of(1,mLabel,1).getMonth().toString();
                    }
                }
                JFreeChart profitChart =Charts.createLineChart(TITLE,VALUES,numbers,CATEGORIES,names,labels);
                CategoryPlot plot = profitChart.getCategoryPlot();
                LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
                renderer.setBaseShapesVisible(true);

                cp = new ChartPanel(profitChart);
                // STATS TEXT
                // MOST PROFITABLE MONTH
                // LEAST PROFITABLE MONTH

                // AVERAGE
                double averageProfit = totalProfit/numbers.size();
                // TOTAL PROFIT
                nF = new DecimalFormat("#0.00");
                textPane.setText(
                        "Most Profitable Month:\n     " + mProfitMonth + ", " + nF.format(mProfit) + " Kr\n" +
                        "Least Profitable Month:\n     " + lProfitMonth + ", " + nF.format(lProfit) + " Kr\n\n" +

                        "Average profit:\n     " + nF.format(averageProfit) + " Kr\n" +
                        "Total profit:\n     " + nF.format(totalProfit)   + " Kr\n"
                );
                textPane.setCaretPosition(0);
                break;
            default:
                updateChartsEvent(BIKE_DISTANCE_CHART);
                break;
        }


        cp.setPreferredSize( new Dimension( 530 , 350 ));
        chartPanel.add(cp);
        mainFrame.revalidate();
    }

    public static void main (String [] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    StatsPanel statsScreen = new StatsPanel(frame);
                    frame.setContentPane(statsScreen);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
