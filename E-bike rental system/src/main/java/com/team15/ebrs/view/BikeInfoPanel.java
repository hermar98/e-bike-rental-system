package main.java.com.team15.ebrs.view;

import java.awt.*;

import javax.swing.JFrame;

import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.dao.DockingStationDAO;
import main.java.com.team15.ebrs.dao.RepairDAO;
import main.java.com.team15.ebrs.dao.TripDAO;
import main.java.com.team15.ebrs.dao.TypeDAO;
import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.BikeData;
import main.java.com.team15.ebrs.data.BikeType;
import main.java.com.team15.ebrs.data.DockingStation;
import main.java.com.team15.ebrs.data.DoneRepair;
import main.java.com.team15.ebrs.data.Repair;
import main.java.com.team15.ebrs.data.RequestedRepair;
import main.java.com.team15.ebrs.util.Calculations;
import main.java.com.team15.ebrs.util.Map;
import main.java.com.team15.ebrs.util.TransparentListCellRenderer;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import org.openstreetmap.gui.jmapviewer.JMapViewer;

import com.toedter.calendar.JDateChooser;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BikeInfoPanel extends JPanel {
	
	private BikeDAO bikeDAO = new BikeDAO();
	private TypeDAO typeDAO = new TypeDAO();
	private RepairDAO repairDAO = new RepairDAO();
	private DockingStationDAO stationDAO = new DockingStationDAO();
	private TripDAO tripDAO = new TripDAO();
	private Bike selectedBike;
	private BikeType selectedBikeType;
	private BikeData selectedBikeData;
	private DockingStation currentStation;
	private ArrayList<Repair> dbRepairList = new ArrayList<Repair>();
	private ArrayList<BikeType> typeList;
	private Repair repair;
	private Calculations calc;
	
	private JButton regRepairButton;
	private JButton editButton;
	private JButton backButton;
	
	private JLabel bikeDataHeader;
	private JTextPane bikeInfo;
	private JTextPane bikeDataInfo;

	private DefaultListModel listModel = new DefaultListModel();
	private JList repairList;
	private JScrollPane scrollPane;
	
	private JFrame mainFrame;
	
	private static final String BG_IMAGE_URL = "/main/resources/com/team15/ebrs/images/BikeInfo.png";
	private static final String OK_ICON_URL = "/main/resources/com/team15/ebrs/images/accept_icon.png";

	/**
	 * BikeInfoPanel will show a panel with the information of the specified bike
	 * @param Bikeid - the id of the specified bike 
	 * @param frame  - the frame 
	 */
	public BikeInfoPanel(int Bikeid, JFrame frame) {
		mainFrame = frame;
		selectedBike = bikeDAO.getBike(Bikeid);
		dbRepairList = repairDAO.getAllRepairs(selectedBike);
		selectedBikeType = selectedBike.getType();
		selectedBikeData = selectedBike.getBikeData();
		currentStation = stationDAO.getStationByID(selectedBike.getStationId());
		calc = new Calculations();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setLayout(null);
		
		JMapViewer mapViewer = Map.getBikeMap(selectedBike.getBikeData());
		JPanel panel = new JPanel(new GridLayout());
		panel.setBounds(525, 58, 285, 201);
		panel.add(mapViewer);
		add(panel);
		
		regRepairButton = new JButton();
		regRepairButton.setBounds(778, 462, 153, 39);
		regRepairButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(regRepairButton);
		
		editButton = new JButton();
		editButton.setBounds(359, 260, 98, 40);
		editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(editButton);
		
		backButton = new JButton();
		backButton.setBounds(56, 0, 69, 71);
		backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(backButton);
		
		ButtonListener bl = new ButtonListener();
		regRepairButton.addActionListener(bl);
		editButton.addActionListener(bl);
		backButton.addActionListener(bl);
		
		hideButton(regRepairButton);
		hideButton(editButton);
		hideButton(backButton);
		
		NumberFormat nf = new DecimalFormat("#0.000");
		
		String bikeInfoText = "Bike ID: " + selectedBike.getBikeId() +
				"\nDate purchased: " + selectedBike.getPurchaseDate() +
				"\nPurchase price: " + selectedBike.getPrice() + " kr" +
				"\n\nMake: " + selectedBike.getMake() +
				"\nType: " + selectedBikeType.getTypeName() +
				"\nRental price: " + selectedBikeType.getRentalPrice() + " kr" +
				"\n\nTotal distance travelled: " + nf.format(calc.getTotalDistanceByID(selectedBike.getBikeId())/1000) + 
				" km\nTotal trips: " + tripDAO.getAllTripsByBikeId(selectedBike.getBikeId()).size();
		bikeInfo = new JTextPane();
		bikeInfo.setBounds(132, 58, 270, 215);
		bikeInfo.setText(bikeInfoText);
		bikeInfo.setEditable(false);
		bikeInfo.setFont(new Font("YuKyokasho Yoko", Font.BOLD, 14));
		bikeInfo.setForeground(Color.WHITE);
		add(bikeInfo);
		
		bikeInfo.setOpaque(false);
		bikeInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
		bikeDataHeader = new JLabel("Most recent data from bike:");
		bikeDataHeader.setBounds(130, 272, 200, 100);
		bikeDataHeader.setFont(new Font("YuKyokasho Yoko", Font.BOLD, 14));
		bikeDataHeader.setForeground(Color.WHITE);
		add(bikeDataHeader);
		
		String bikeDataInfoText = "\nLocation: " 
				+ selectedBikeData.getLatitude() + ", " + selectedBikeData.getLongitude() +
				"\nTime: " + selectedBikeData.getDateTime().toLocalTime() + 
				"\nDate: " + selectedBikeData.getDateTime().toLocalDate() +
				"\nCharging level: " + selectedBikeData.getChargingLvl() + " %";
		if (currentStation != null) {
			bikeDataInfoText += "\n\nCurrently at this station: " + currentStation.getStationName();
		}
		bikeDataInfo = new JTextPane();
		bikeDataInfo.setBounds(130, 325, 278, 148);
		bikeDataInfo.setText(bikeDataInfoText);
		bikeDataInfo.setEditable(false);
		bikeDataInfo.setFont(new Font("YuKyokasho Yoko", Font.BOLD, 13));
		bikeDataInfo.setForeground(Color.WHITE);
		add(bikeDataInfo);
		
		bikeDataInfo.setOpaque(false);
		bikeDataInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
		for(Repair repair : dbRepairList) {
			listModel.addElement(repair);
		}
		
		repairList = new JList(listModel);
		repairList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		repairList.setFont(new Font("YuKyokasho Yoko", Font.PLAIN, 14));
		repairList.setCellRenderer(new TransparentListCellRenderer());
		repairList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    	
		        JList list = (JList) e.getSource();
		  
		        if (e.getClickCount() == 2) {      // Double-click detected
		        	Repair repair = (Repair) list.getSelectedValue();
		        	RepairStatusDialog statusDialog = new RepairStatusDialog(repair, selectedBike, listModel);
		        	statusDialog.setVisible(true);
		        }
		    }
		});
		add(repairList);
		
		repairList.setOpaque(false);
		repairList.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
		scrollPane = new JScrollPane(repairList);
		scrollPane.setBounds(502, 285, 350, 160);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(scrollPane);
		
		/* Load background image */
		URL bgUrl = getClass().getResource(BG_IMAGE_URL);
		ImageIcon bgImage = new ImageIcon(bgUrl);
		
		JLabel background = new JLabel(bgImage);
		background.setBounds(0, 0, 960, 518);
		add(background);
	}

	public class ButtonListener implements ActionListener {
		/**
		 *Invoked when an action occurs.
		 * @param action
		 */
		public void actionPerformed (ActionEvent action) {
			  
			JButton clickedButton = (JButton) action.getSource();
			  
			if(clickedButton.equals(regRepairButton)) {
				JLabel label = new JLabel("Select what kind of repair you want to register: ");
				String[] choices = {"Requested repair", "Done repair"};
				JComboBox choiceList = new JComboBox(choices);
				
				label.setBounds(0, 0, 300, 30);
				choiceList.setBounds(42, 30, 150, 20);
				
				JPanel selectPanel = new JPanel();
	   		  	selectPanel.setLayout(null);
	   		  	selectPanel.add(label);
	   		  	selectPanel.add(choiceList);
	   		  	
	   		  	UIManager.put("OptionPane.minimumSize", new Dimension(350, 120));
				int result = JOptionPane.showConfirmDialog(null, selectPanel, 
			    		"Select repair type", JOptionPane.OK_CANCEL_OPTION);
				UIManager.put("OptionPane.minimumSize", null);
				if (result == JOptionPane.OK_OPTION && choiceList.getSelectedIndex() == 0) {
					JLabel registerInfo = new JLabel("Please fill in the info below: ");
					JLabel dateLabel = new JLabel("Date sent: ");
					JLabel descLabel = new JLabel("Request description: ");
					JTextField descField = new JTextField();
					
					JDateChooser dateField = new JDateChooser();
		   		  	dateField.setDateFormatString("yyyy-MM-dd");
		   		  	Date todaysDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		   		  	dateField.setDate(todaysDate);
					
					registerInfo.setBounds(0, 0, 300, 20);
					dateLabel.setBounds(0, 25, 300, 20);
					dateField.setBounds(0, 45, 150, 20);
					descLabel.setBounds(0, 70, 300, 20);
					descField.setBounds(0, 90, 150, 20);
					
					JPanel addRequestPanel = new JPanel();
		   		  	addRequestPanel.setLayout(null);
		   		  	addRequestPanel.add(registerInfo);
		   		  	addRequestPanel.add(dateLabel);
		   		  	addRequestPanel.add(dateField);
			   		addRequestPanel.add(descLabel);
			   		addRequestPanel.add(descField);
			   		
					UIManager.put("OptionPane.minimumSize", new Dimension(250, 180));
					int response = JOptionPane.showConfirmDialog(null, addRequestPanel, 
				    		"Register repair", JOptionPane.OK_CANCEL_OPTION);
					UIManager.put("OptionPane.minimumSize", null);
					
					if (response == JOptionPane.OK_OPTION) {
						LocalDate date = dateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						String desc = descField.getText();
						RequestedRepair rRepair = new RequestedRepair(date, desc);
						try {
							int repairId = repairDAO.addRepair(rRepair, selectedBike);
							URL okIconUrl = getClass().getResource(OK_ICON_URL);
	   		  				ImageIcon okIcon = new ImageIcon(okIconUrl);
	   		  				JOptionPane.showMessageDialog(null,
	   		  						"The repair was successfully registered.",
	   		  						"Repair registered!",
	   		  						JOptionPane.INFORMATION_MESSAGE, okIcon);
	   		  				rRepair.setRepairId(repairId);
	   		  				listModel.addElement(rRepair);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null,
	   		  						"Something went wrong!\nThe repair was not registered.",
	   		  						"Error!",
	   		  						JOptionPane.WARNING_MESSAGE);
						}
					}
				} else if (result == JOptionPane.OK_OPTION && choiceList.getSelectedIndex() == 1) {
					JLabel registerInfo = new JLabel("Please fill in the info below: ");
					JLabel dateLabel = new JLabel("Date sent: ");
					JLabel dateLabel2 = new JLabel("Date finished: ");
					JLabel descLabel = new JLabel("Request description: ");
					JLabel descLabel2 = new JLabel("Repair description: ");
					JLabel priceLabel = new JLabel("Repair price: ");
					JTextField descField = new JTextField();
					JTextField descField2 = new JTextField();
					JTextField priceField = new JTextField();
					
					JDateChooser dateField = new JDateChooser();
		   		  	dateField.setDateFormatString("yyyy-MM-dd");
		   		  	Date todaysDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		   		  	dateField.setDate(todaysDate);
		   		  	
		   		  	JDateChooser dateField2 = new JDateChooser();
		   		  	dateField2.setDateFormatString("yyyy-MM-dd");
		   		  	dateField2.setDate(todaysDate);
		   		  	
		   		  	registerInfo.setBounds(0, 0, 300, 20);
					dateLabel.setBounds(0, 25, 300, 20);
					dateField.setBounds(0, 45, 150, 20);
					dateLabel2.setBounds(0, 70, 300, 20);
					dateField2.setBounds(0, 90, 150, 20);
					descLabel.setBounds(0, 115, 300, 20);
					descField.setBounds(0, 135, 150, 20);
					descLabel2.setBounds(0, 160, 300, 20);
					descField2.setBounds(0, 180, 150, 20);
					priceLabel.setBounds(0, 205, 300, 20);
					priceField.setBounds(0, 225, 150, 20);
					
					JPanel addDonePanel = new JPanel();
		   		  	addDonePanel.setLayout(null);
		   		  	addDonePanel.add(registerInfo);
		   		  	addDonePanel.add(dateLabel);
		   		  	addDonePanel.add(dateField);
		   		  	addDonePanel.add(dateLabel2);
		   		  	addDonePanel.add(dateField2);
			   		addDonePanel.add(descLabel);
			   		addDonePanel.add(descField);
			   		addDonePanel.add(descLabel2);
			   		addDonePanel.add(descField2);
			   		addDonePanel.add(priceLabel);
			   		addDonePanel.add(priceField);
			   		
			   		UIManager.put("OptionPane.minimumSize", new Dimension(250, 310));
					int response = JOptionPane.showConfirmDialog(null, addDonePanel, 
				    		"Register repair", JOptionPane.OK_CANCEL_OPTION);
					UIManager.put("OptionPane.minimumSize", null);
					
					if (response == JOptionPane.OK_OPTION) {
						try {
							double price = Double.parseDouble(priceField.getText());
							LocalDate dateSent = dateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
							LocalDate dateRecieved = dateField2.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
							String requestDesc = descField.getText();
							String repairDesc = descField2.getText();
							if (dateRecieved.isBefore(dateSent)) {
								JOptionPane.showMessageDialog(null,
		   		  						"Date finished can not be earlier than the date sent!",
		   		  						"Error!",
		   		  						JOptionPane.WARNING_MESSAGE);
							} else {
								DoneRepair dRepair = new DoneRepair(price, dateSent, dateRecieved, requestDesc, repairDesc);
								try {
									int repairId = repairDAO.addRepair(dRepair, selectedBike);
									URL okIconUrl = getClass().getResource(OK_ICON_URL);
			   		  				ImageIcon okIcon = new ImageIcon(okIconUrl);
			   		  				JOptionPane.showMessageDialog(null,
			   		  						"The repair was successfully registered.",
			   		  						"Repair registered!",
			   		  						JOptionPane.INFORMATION_MESSAGE, okIcon);
			   		  				dRepair.setRepairId(repairId);
			   		  				listModel.addElement(dRepair);
								} catch (Exception e) {
									JOptionPane.showMessageDialog(null,
			   		  						"Something went wrong!\nThe repair was not registered.",
			   		  						"Error!",
			   		  						JOptionPane.WARNING_MESSAGE);
								}
							}
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(null,
	   		  						"The price has to be a number!",
	   		  						"Error!",
	   		  						JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			} else if (clickedButton.equals(editButton)) {
				typeList = typeDAO.getAllTypes();
				JTextField makeField = new JTextField(10);
				JComboBox selectType = new JComboBox(typeList.toArray());
				JLabel editInfo = new JLabel("Please fill out the new bike info: ");
				JLabel makeLabel = new JLabel("Make: ");
				JLabel typeLabel = new JLabel("Type: ");
				
				makeField.setBounds(0, 45, 150, 20);
				selectType.setBounds(0, 90, 150, 20);
				editInfo.setBounds(0, 0, 300, 20);
				makeLabel.setBounds(0, 25, 300, 20);
				typeLabel.setBounds(0, 70, 300, 20);
				
				JPanel editPanel = new JPanel();
				editPanel.setLayout(null);
			    editPanel.add(editInfo);
			    editPanel.add(makeLabel);
			    editPanel.add(makeField);
			    editPanel.add(typeLabel);
			    editPanel.add(selectType);
			    
			    UIManager.put("OptionPane.minimumSize", new Dimension(300, 175));
			    int result = JOptionPane.showConfirmDialog(null, editPanel, 
			    		"Edit bike", JOptionPane.OK_CANCEL_OPTION);
			    UIManager.put("OptionPane.minimumSize", null);
			    
			    if (result == JOptionPane.OK_OPTION) {
			    	if (makeField.getText().equals("")) {
			    		JOptionPane.showMessageDialog(mainFrame,
   		  						"Bike not edited! Please fill in the new make.",
   		  						"Error!",
   		  						JOptionPane.WARNING_MESSAGE);
			    	} else {
			    		selectedBike.setMake(makeField.getText());
			    		selectedBikeType = (BikeType) selectType.getSelectedItem();
			    		selectedBike.setType(selectedBikeType);
			    		if (bikeDAO.editBike(selectedBike)) {
				    		URL okIconUrl = getClass().getResource(OK_ICON_URL);
		   			  		ImageIcon okIcon = new ImageIcon(okIconUrl);
		   			  		JOptionPane.showMessageDialog(mainFrame,
		   			  				"The bike was successfully edited.",
		   			  				"Password changed!",
		   			  				JOptionPane.INFORMATION_MESSAGE, okIcon);
		   			  		String bikeInfoText = "Bike ID: " + selectedBike.getBikeId() +
		   			  				"\nDate purchased: " + selectedBike.getPurchaseDate() +
		   			  				"\nPurchase price: " + selectedBike.getPrice() + " kr" +
		   			  				"\n\nMake: " + selectedBike.getMake() +
		   			  				"\nType: " + selectedBikeType.getTypeName() +
		   			  				"\nRental price: " + selectedBikeType.getRentalPrice() + " kr" +
		   			  				"\n\nTotal km travelled: " + "\nTotal trips: ";
		   			  		bikeInfo.setText(bikeInfoText);
			    		} else {
			    			JOptionPane.showMessageDialog(mainFrame,
	   		  						"Something went wrong!\nThe bike was not edited.",
	   		  						"Error!",
	   		  						JOptionPane.WARNING_MESSAGE);
			    		}
			    	}
			    }
			} else if (clickedButton.equals(backButton)) {  
				BikePanel bikeScreen = new BikePanel(mainFrame);
				mainFrame.setContentPane(bikeScreen);
				mainFrame.revalidate();
			}
		}	
	}
	 
	public class TransparentListCellRenderer extends DefaultListCellRenderer {
		/**
		 *Return a component that has been configured to display the specified value.
		 * @param list - The JList we're painting.
		 * @param value  - The value returned by list.getModel().getElementAt(index).
		 * @param index	- The cells index.
		 * @param isSelected - True if the specified cell was selected.
		 * @param cellHasFocus  - True if the specified cell has the focus.
		 * @return A component whose paint() method will render the specified value.
		 */
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            setForeground(Color.BLACK);
            setOpaque(isSelected);
            return this;
        }
	}

	/**
	 * a private method for hiding the button on the screen .
	 * @param button - the button to hide
	 */
 	private void hideButton(JButton button) {
 		button.setOpaque(false);
 		button.setContentAreaFilled(false);
 		button.setBorderPainted(false);
 	}

	/*  ------------main class ---------------*/
	public static void main(String[] args) {
	  EventQueue.invokeLater(new Runnable() {
		  public void run() {
			  try {
				  MainFrame frame = new MainFrame();
				  BikeInfoPanel bikeInfoScreen = new BikeInfoPanel(1000, frame);
				  frame.setContentPane(bikeInfoScreen);
				  frame.setVisible(true);
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
		  }
	  });	
	}
}
