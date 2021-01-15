package main.java.com.team15.ebrs.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.dao.DockingStationDAO;
import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.DockingStation;
import main.java.com.team15.ebrs.data.BikeType;

import java.net.URL;

import main.java.com.team15.ebrs.dao.TypeDAO;

/**
 * AddBikePanel is a container class with frame with the specified layout for adding a new bike in database
 *
 * @author Team 15
 */
public class AddBikePanel extends JPanel {
    private BikeDAO bikeDAO = new BikeDAO();
    private TypeDAO typeDAO = new TypeDAO();
    private DockingStationDAO stationDAO = new DockingStationDAO();
    private int price;
    private LocalDate date;
    private String make;
    private int bike_id;
    private BikeType bike_type;
    private Date todaysDate;
    private ArrayList<String> typecombo = new ArrayList<String>();
    private ArrayList<String> dockCombo = new ArrayList<String>();
    private ArrayList<DockingStation> stationList;
    private ArrayList<BikeType> typeList;
    private Bike newbike;
    private JDateChooser dateChooser;
    private DockingStation dock_chosen;

    private JButton backButton;
    private JButton saveButton;

    private JTextField textMake;
    private JTextField textPrice;
    private JComboBox selectType;
    private JComboBox selectStation;

    private JFrame mainFrame;

    private static final String BG_IMAGE_URL = "/main/resources/com/team15/ebrs/images/NewBike.png";
    private static final String OK_ICON_URL = "/main/resources/com/team15/ebrs/images/accept_icon.png";

    /**
     * Create the application.
     */
    public AddBikePanel(JFrame frame) {
        mainFrame = frame;
        typeList = typeDAO.getAllTypes();
        stationList = stationDAO.getAllDockingStations();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setLayout(null);

        /* Adding the buttons */
        backButton = new JButton();
        backButton.setBounds(38, 5, 97, 86);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(backButton);

        saveButton = new JButton();
        saveButton.setBounds(751, 401, 91, 78);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(saveButton);

        ButtonListener bl = new ButtonListener();
        backButton.addActionListener(bl);
        saveButton.addActionListener(bl);

        hideButton(backButton);
        hideButton(saveButton);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(214, 124, 199, 34);
        dateChooser.setDateFormatString("yyyy-MM-dd");
        todaysDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        dateChooser.setDate(todaysDate);
        dateChooser.setFont(new Font("Verdana", Font.PLAIN, 14));
        add(dateChooser);

        textMake = new JTextField();
        textMake.setBounds(217, 248, 190, 28);
        textMake.setFont(new Font("Verdana", Font.PLAIN, 16));
        add(textMake);

        textMake.setOpaque(false);
        textMake.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        textPrice = new JTextField();
        textPrice.setBounds(501, 127, 190, 28);
        textPrice.setFont(new Font("Verdana", Font.PLAIN, 16));
        add(textPrice);

        textPrice.setOpaque(false);
        textPrice.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        selectType = new JComboBox(typeList.toArray());
        selectType.setBounds(500, 245, 198, 34);
        selectType.setFont(new Font("Verdana", Font.PLAIN, 16));
        selectType.setOpaque(false);
        selectType.setRenderer(new TransparentListCellRenderer());
        add(selectType);

        ArrayList<String> stationNameList = new ArrayList<String>();
        stationNameList.add("Not at station");
        for (DockingStation ds : stationList) {
            stationNameList.add(ds.getStationName());
        }
        selectStation = new JComboBox(stationNameList.toArray());
        selectStation.setBounds(499, 375, 198, 34);
        selectStation.setFont(new Font("Verdana", Font.PLAIN, 16));
        selectStation.setOpaque(false);
        selectStation.setRenderer(new TransparentListCellRenderer());
        add(selectStation);

        /* Load background image */
        URL bgUrl = getClass().getResource(BG_IMAGE_URL);
        ImageIcon bgImage = new ImageIcon(bgUrl);

        JLabel background = new JLabel(bgImage);
        background.setBounds(0, 0, 960, 520);
        add(background);
    }


    private class ButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param action - the action when the button is clicked
         */
        public void actionPerformed(ActionEvent action) {

            JButton clickedButton = (JButton) action.getSource();

            if (clickedButton.equals(saveButton)) {
                Date selectedDate = dateChooser.getDate();
                if (selectedDate == null || textPrice.getText().equals("")
                        || textMake.getText().equals("")) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Please fill out all the fields.",
                            "Error!",
                            JOptionPane.WARNING_MESSAGE);
                } else if (selectedDate.after(todaysDate)) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "The date can not be a future date!",
                            "Error!",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        LocalDate date = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        double price = Double.parseDouble(textPrice.getText());
                        String make = textMake.getText();
                        BikeType type = (BikeType) selectType.getSelectedItem();
                        int stationId = 0;
                        String stationName = (String) selectStation.getSelectedItem();
                        DockingStation station = null;
                        for (DockingStation ds : stationList) {
                            if (stationName.equals(ds.getStationName())) {
                                station = ds;
                                stationId = ds.getStationId();
                            }
                        }
                        Bike newBike = new Bike(date, price, make, type, stationId);
                        if (bikeDAO.addBike(newBike, station)) {
                            URL okIconUrl = getClass().getResource(OK_ICON_URL);
                            ImageIcon okIcon = new ImageIcon(okIconUrl);
                            JOptionPane.showMessageDialog(mainFrame,
                                    "The new bike was successfully registered.",
                                    "Bike registered!",
                                    JOptionPane.INFORMATION_MESSAGE, okIcon);
                            BikePanel bikeScreen = new BikePanel(mainFrame);
                            mainFrame.setContentPane(bikeScreen);
                            mainFrame.revalidate();
                        } else {
                            JOptionPane.showMessageDialog(mainFrame,
                                    "Something went wrong!\nBike not registered.",
                                    "Error!",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(mainFrame,
                                "The bike price has to be a number.",
                                "Error!",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else if (clickedButton.equals(backButton)) {
                BikePanel bikePanel = new BikePanel(mainFrame);
                mainFrame.setContentPane(bikePanel);
                mainFrame.revalidate();
            }
        }
    }

    public class TransparentListCellRenderer extends DefaultListCellRenderer {
        /**
         * Return a component that has been configured to display the specified value.
         *
         * @param list         - The JList we're painting.
         * @param value        - The value returned by list.getModel().getElementAt(index).
         * @param index        - The cells index.
         * @param isSelected   - True if the specified cell was selected.
         * @param cellHasFocus - True if the specified cell has the focus.
         * @return A component whose paint() method will render the specified value.
         */
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            setForeground(Color.DARK_GRAY);
            setOpaque(isSelected);
            return this;
        }
    }

    /**
     * a private method for hiding the button on the screen .
     *
     * @param button - the button to hide
     */
    private void hideButton(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    AddBikePanel addBikeScreen = new AddBikePanel(frame);
                    frame.setContentPane(addBikeScreen);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}