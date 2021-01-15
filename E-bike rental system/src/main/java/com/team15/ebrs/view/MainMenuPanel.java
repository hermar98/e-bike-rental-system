
package main.java.com.team15.ebrs.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
/**
 * MainMenuPanel is a container class with frame with the specified layout for the main menu of the whole programmer
 * @author Team 15
 *
 */
public class MainMenuPanel extends JPanel {
	
	private JFrame mainFrame;
	private JButton usersButton;
	private JButton bikesButton;
	private JButton stationsButton;
	private JButton statsButton;
	private JButton mapButton;
	private JButton logoutButton;
	
	private static final String BG_IMAGE_URL = "/main/resources/com/team15/ebrs/images/mainmenu.png";
	
	/**
	 * Create the JPanel.
	 */
	public MainMenuPanel(JFrame frame) {
		mainFrame = frame;
		initialize();
	}

	/**
	 * Initialize the contents of the panel.
	 */
	private void initialize() {
		setLayout(null);
		
		/* Add the buttons */
		usersButton = new JButton();
		usersButton.setBounds(0, 366, 239, 145);
		usersButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(usersButton);
		
		bikesButton = new JButton();
		bikesButton.setBounds(240, 366, 240, 145);
		bikesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(bikesButton);
		
		stationsButton = new JButton();
		stationsButton.setBounds(481, 366, 256, 145);
		stationsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    add(stationsButton);
		
		statsButton = new JButton();
		statsButton.setBounds(738, 366, 216, 145);
		statsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(statsButton);
		
		mapButton = new JButton();
		mapButton.setBounds(893, 0, 61, 57);
		mapButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(mapButton);
		
		logoutButton = new JButton();
		logoutButton.setBounds(0, 0, 61, 57);
		logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(logoutButton);
		
		/* Hide the buttons */
		hideButton(usersButton);
		hideButton(bikesButton);
		hideButton(stationsButton);
		hideButton(statsButton);
		hideButton(mapButton);
		hideButton(logoutButton);
		
		/* Add a ButtonListener-object to the buttons */
		ButtonListener bl = new ButtonListener();
	    usersButton.addActionListener(bl);
	    bikesButton.addActionListener(bl);
	    stationsButton.addActionListener(bl);
	    statsButton.addActionListener(bl);
	    mapButton.addActionListener(bl);
	    logoutButton.addActionListener(bl);
	    
	    /* Load the background */
		URL bgUrl = getClass().getResource(BG_IMAGE_URL);
		ImageIcon bgImage = new ImageIcon(bgUrl);
		
		JLabel background = new JLabel(bgImage);
		background.setBounds(0, 0, 960, 540);
		add(background);
	}
	
	/* Class that takes care of button interactions */
	class ButtonListener implements ActionListener {
		 /**
         * Invoked when an action occurs.
         * @param e - the action when the button is clicked
         */
		public void actionPerformed(ActionEvent e) {
			
			JButton clickedButton = (JButton) e.getSource();

   		  	if (clickedButton.equals(usersButton)) {
   		  		UserPanel userScreen = new UserPanel(mainFrame);
   		  		mainFrame.setContentPane(userScreen);
   		  		mainFrame.revalidate();
   		  	} else if (clickedButton.equals(bikesButton)) {
   		  		BikePanel bikeScreen = new BikePanel(mainFrame);
   		  		mainFrame.setContentPane(bikeScreen);
   		  		mainFrame.revalidate();
   		  	} else if (clickedButton.equals(stationsButton)) {
   			  	StationPanel dockingStationScreen = new StationPanel(mainFrame);
   			  	mainFrame.setContentPane(dockingStationScreen);
   			  	mainFrame.revalidate();
   		  	} else if (clickedButton.equals(statsButton)) {
	   		  	StatsPanel statsScreen = new StatsPanel(mainFrame);
			  	mainFrame.setContentPane(statsScreen);
			  	mainFrame.revalidate();
   		  	} else if (clickedButton.equals(mapButton)) {
   			  	BikeStationMapPanel mapScreen = new BikeStationMapPanel(mainFrame);
			  	mainFrame.setContentPane(mapScreen);
			  	mainFrame.revalidate();
   		  	} else if (clickedButton.equals(logoutButton)) {
   		  		LoginPanel loginScreen = new LoginPanel(mainFrame);
			  	mainFrame.setContentPane(loginScreen);
			  	mainFrame.revalidate();
   		  	}
   	  	}
   	}
	
	/**
	 * Method to make the buttons transparent
	 */
	private void hideButton(JButton button) {
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					MainMenuPanel mainMenuScreen = new MainMenuPanel(frame);
					frame.setContentPane(mainMenuScreen);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}