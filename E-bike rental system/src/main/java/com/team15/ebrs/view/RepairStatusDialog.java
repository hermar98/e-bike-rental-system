package main.java.com.team15.ebrs.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import com.toedter.calendar.JDateChooser;

import main.java.com.team15.ebrs.dao.RepairDAO;
import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.DoneRepair;
import main.java.com.team15.ebrs.data.Repair;
import main.java.com.team15.ebrs.data.RequestedRepair;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
/**
 * registerRepair is a container class with frame with the specified layout for showing the status of the repair for a bike
 * @author Team 15
 *
 */
public class RepairStatusDialog extends JDialog {

	private Repair selectedRepair;
	private Bike selectedBike;
	private RepairDAO repairDAO = new RepairDAO();
	
	private JTextPane repairInfo;
	
	private JButton deleteButton;
	private JButton updateButton;
	private JButton exitButton;
	
	private DefaultListModel repairList;
	
	private static final String BG_IMAGE_URL = "/main/resources/com/team15/ebrs/images/repairstatus.png";
	private static final String BG_IMAGE_URL_2 = "/main/resources/com/team15/ebrs/images/repairstatus2.png";
	private static final String OK_ICON_URL = "/main/resources/com/team15/ebrs/images/accept_icon.png";

	/**
	 * Create the application.
	 */
	public RepairStatusDialog(Repair repair, Bike bike, DefaultListModel repairList) {
		this.repairList = repairList;
		selectedBike = bike;
		selectedRepair = repair;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setSize(450, 328);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);
		
		exitButton = new JButton();
		exitButton.setBounds(5, 0, 73, 68);
		hideButton(exitButton);
		add(exitButton);
		
		deleteButton = new JButton();
		hideButton(deleteButton);
		add(deleteButton);
		
		ButtonListener bl = new ButtonListener();
		exitButton.addActionListener(bl);
		deleteButton.addActionListener(bl);
		
		if (selectedRepair instanceof RequestedRepair) {
			deleteButton.setBounds(344, 253, 98, 33);
			updateButton = new JButton();
			updateButton.setBounds(144, 257, 182, 28);
			updateButton.addActionListener(bl);
			hideButton(updateButton);
			add(updateButton);
		} else {
			deleteButton.setBounds(315, 253, 98, 33);
		}
		
		repairInfo = new JTextPane();
		repairInfo.setEditable(false);
		if (selectedRepair instanceof RequestedRepair) {
			repairInfo.setBounds(82, 50, 277, 190);
		} else {
			repairInfo.setBounds(95, 50, 277, 190);
		}
		repairInfo.setFont(new Font("YuKyokasho Yoko", Font.BOLD, 13));
		repairInfo.setForeground(Color.BLACK);
		repairInfo.setOpaque(false);
		repairInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
		String repairInfoString = "Repair ID: " + selectedRepair.getRepairId() + 
				"\nDate Requested: " + selectedRepair.getDateSent() + 
				"\nRequest description: " +  selectedRepair.getRequestDesc();

		if (selectedRepair instanceof RequestedRepair) {
			repairInfoString += "\n\nStatus: In progress";
		} else if (selectedRepair instanceof DoneRepair) {
			DoneRepair doneRepair = (DoneRepair) selectedRepair;
			repairInfoString += "\n\nDate finished: " + doneRepair.getDateReceived() +
					"\nRepair description: " + doneRepair.getRepairDesc() + 
					"\nPrice: " + doneRepair.getPrice() + " kr\n\nStatus: Done";
		}
		
		repairInfo.setText(repairInfoString);
		add(repairInfo);
		
		URL bgUrl = null;
		ImageIcon bgImage = null;
		
		if (selectedRepair instanceof DoneRepair) {
			bgUrl = getClass().getResource(BG_IMAGE_URL);
			bgImage = new ImageIcon(bgUrl);
		} else {
			bgUrl = getClass().getResource(BG_IMAGE_URL_2);
			bgImage = new ImageIcon(bgUrl);
		}
		
		JLabel background = new JLabel(bgImage);
		background.setBounds(0, 0, 450, 300);
		add(background);
	}
	
	public class ButtonListener implements ActionListener {
		/**
         * Invoked when an action occurs.
         * @param action - the action when the button is clicked 
         */
		public void actionPerformed (ActionEvent action) {
			  
			JButton clickedButton = (JButton) action.getSource();
			  
			if(clickedButton.equals(exitButton)) {
				setVisible(false);
				dispose();
			} else if (clickedButton.equals(updateButton)) {
				JLabel updateInfo = new JLabel("Please fill in the fields below: ");
				JLabel priceLabel = new JLabel("Repair price: ");
				JLabel descLabel = new JLabel("Repair description: ");
				JLabel dateLabel = new JLabel("Date the repair was finished: ");
				JTextField priceField = new JTextField();
	   		  	JTextField descField = new JTextField();
	   		  	
	   		  	JDateChooser dateField = new JDateChooser();
	   		  	dateField.setDateFormatString("yyyy-MM-dd");
	   		  	Date todaysDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
	   		  	dateField.setDate(todaysDate);
	   		  	
	   		  	updateInfo.setBounds(0, 0, 300, 20);
	   		  	dateLabel.setBounds(0, 25, 300, 20);
	   		  	dateField.setBounds(0, 45, 150, 20);
	   		  	descLabel.setBounds(0, 70, 300, 20);
	   		  	descField.setBounds(0, 90, 150, 20);
	   		  	priceLabel.setBounds(0, 115, 300, 20);
	   		  	priceField.setBounds(0, 135, 150, 20);
	   		  	
	   		  	JPanel updatePanel = new JPanel();
	   		  	updatePanel.setLayout(null);
	   		  	updatePanel.add(updateInfo);
	   		  	updatePanel.add(priceLabel);
	   		  	updatePanel.add(priceField);
		   		updatePanel.add(dateLabel);
		   		updatePanel.add(dateField);
		   		updatePanel.add(descLabel);
		   		updatePanel.add(descField);
	   		  	
	   		  	UIManager.put("OptionPane.minimumSize", new Dimension(300, 230));
			    int result = JOptionPane.showConfirmDialog(null, updatePanel, 
			    		"Update repair", JOptionPane.OK_CANCEL_OPTION);
			    UIManager.put("OptionPane.minimumSize", null);
			    
			    if (result == JOptionPane.YES_OPTION) {
			    	try {
			    		double price = Double.parseDouble(priceField.getText());
			    		LocalDate date = dateField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			    		String desc = descField.getText();
			    		if (date.isBefore(selectedRepair.getDateSent())) {
			    			JOptionPane.showMessageDialog(null,
	   		  						"Date finished can not be earlier than the date sent!",
	   		  						"Error!",
	   		  						JOptionPane.WARNING_MESSAGE);
			    		} else {
			    			try {
			    				DoneRepair dRepair = new DoneRepair(selectedRepair.getRepairId(), price,
			    						selectedRepair.getDateSent(), date, selectedRepair.getRequestDesc(),
			    						desc);
			    				repairDAO.updateRepair(dRepair, selectedBike);
			    				URL okIconUrl = getClass().getResource(OK_ICON_URL);
		   		  				ImageIcon okIcon = new ImageIcon(okIconUrl);
		   		  				JOptionPane.showMessageDialog(null,
		   		  						"The repair was successfully updated.",
		   		  						"Repair updated!",
		   		  						JOptionPane.INFORMATION_MESSAGE, okIcon);
		   		  				int index = repairList.indexOf(selectedRepair);
		   		  				repairList.setElementAt(dRepair, index);
		   		  				RepairStatusDialog newDialog = new RepairStatusDialog(dRepair,
		   		  						selectedBike, repairList);
		   		  				setVisible(false);
		   		  				newDialog.setVisible(true);
		   		  				dispose();
			    			} catch (Exception e) {
			    				JOptionPane.showMessageDialog(null,
		   		  						"Something went wrong!\nThe repair was not updated.",
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
			} else if (clickedButton.equals(deleteButton)) {
				int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this repair?", 
						"Warning!", JOptionPane.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION) {
					try {
						repairDAO.deleteRepair(selectedRepair);
						URL okIconUrl = getClass().getResource(OK_ICON_URL);
   		  				ImageIcon okIcon = new ImageIcon(okIconUrl);
   		  				JOptionPane.showMessageDialog(null,
   		  						"The repair was successfully deleted.",
   		  						"Repair deleted!",
   		  						JOptionPane.INFORMATION_MESSAGE, okIcon);
   		  				repairList.removeElement(selectedRepair);
   		  				setVisible(false);
   		  				dispose();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
   		  						"Something went wrong!\nThe repair was not deleted.",
   		  						"Error!",
   		  						JOptionPane.WARNING_MESSAGE);
					}
				}
			}
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
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				DoneRepair dRepair = new DoneRepair(70, LocalDate.now(), LocalDate.now(), "Test", "Test2");
				RequestedRepair rRepair = new RequestedRepair(LocalDate.now(), "Test");
				//RepairStatusDialog dialog = new RepairStatusDialog(dRepair);
				//dialog.setVisible(true);
			}
		});
	}
}
