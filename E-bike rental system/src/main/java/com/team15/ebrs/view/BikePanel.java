package main.java.com.team15.ebrs.view;

import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.data.Bike;
import main.java.com.team15.ebrs.data.BikeType;
import main.java.com.team15.ebrs.data.User;
import main.java.com.team15.ebrs.util.TransparentListCellRenderer;

import java.awt.*;


import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;

import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import main.java.com.team15.ebrs.dao.TypeDAO;

/**
 * BikePanel is a container class with frame with the specified layout for showing all the available bikes in the database. 
 * @author Team 15
 *
 */

public class BikePanel extends JPanel {
	
	private JTextField searchBar;
	private DefaultListModel listModel = new DefaultListModel();
	private ArrayList<String> searchList= new ArrayList<String>();
	private DefaultListModel bikelist2 = new DefaultListModel();
	private int bikeID;
	private JList bikeList;
	private JScrollPane scrollPane;
	
	private JButton addButton;
	private JButton deleteButton;
	private JButton typesButton;
	private JButton backButton;
	
	private BikeDAO bikeDAO = new BikeDAO();
    private ArrayList<Bike> dbBikeList;
	
	private JFrame mainFrame;
	
	private static final String BG_IMAGE_URL = "/main/resources/com/team15/ebrs/images/bikes_window.png";
	private static final String OK_ICON_URL = "/main/resources/com/team15/ebrs/images/accept_icon.png";

	/**
	 * The frame to be added to the panel
	 * @param frame
	 */
	public BikePanel(JFrame frame) {
		mainFrame = frame;
		dbBikeList = bikeDAO.getAllBikes();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setLayout(null);
		
		/* Adding buttons */
		addButton = new JButton();
		addButton.setBounds(647, 475, 29, 29);
		addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(addButton);
		
		deleteButton = new JButton();
		deleteButton.setBounds(901, 475, 23, 29);
		deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(deleteButton);
		
		typesButton = new JButton();
		typesButton.setBounds(733, 475, 110, 29);
		typesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(typesButton);
		
		backButton = new JButton();
		backButton.setBounds(25, 12, 75, 75);
		backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(backButton);

		/* Adding ButtonListener-object to the buttons */
		ButtonListener bl = new ButtonListener();
		addButton.addActionListener(bl);
		deleteButton.addActionListener(bl);
		typesButton.addActionListener(bl);
		backButton.addActionListener(bl);
		
		/* Hide the buttons */
		hideButton(addButton);
		hideButton(deleteButton);
		hideButton(typesButton);
		hideButton(backButton);
		
		/* Adding the Search Bar */
		searchBar = new JTextField();
		searchBar.setBounds(640, 43, 258, 30);
		searchBar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String searchText = searchBar.getText();
			    listModel.removeAllElements();
				for (Bike bike : dbBikeList) {
					if(bike.toString().toLowerCase().contains(searchText.toLowerCase())) {
						listModel.addElement(bike);
					} 
				}
			}
		});
		searchBar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
		searchBar.setForeground(Color.DARK_GRAY);
		add(searchBar);
		
		/* Hide the Search Bar */
		searchBar.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		searchBar.setOpaque(false);
		
		listModel = new DefaultListModel();
		for (Bike bike: dbBikeList) {
            listModel.addElement(bike);
        }
		
        bikeList = new JList(listModel);
        bikeList.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        bikeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
		bikeList.setOpaque(false);
		bikeList.setCellRenderer(new TransparentListCellRenderer());
        bikeList.setSelectedIndex(0);
        
        bikeList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JList list = (JList) e.getSource();
					Bike bike = (Bike) list.getSelectedValue();
		        	BikeInfoPanel bikeInfoPanel = new BikeInfoPanel(bike.getBikeId(), mainFrame);
		        	mainFrame.setContentPane(bikeInfoPanel);
		        	mainFrame.revalidate();
		        } 
			}
		});
		
		scrollPane = new JScrollPane(bikeList);
		scrollPane.setBounds(640, 105, 292, 328);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

		add(scrollPane);
			
		/* Load the background */
		URL bgUrl = getClass().getResource(BG_IMAGE_URL);
		ImageIcon bgImage = new ImageIcon(bgUrl);
			
		JLabel background = new JLabel(bgImage);
		background.setBounds(0, 0, 960, 520);
		add(background);
	}
	
	/* Replacing the JList's cell renderer with one that makes it transparent */
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
            
            setForeground(Color.WHITE);
            setOpaque(isSelected);
            return this;
        }

    }
	
	
	/**
	 * Invoked when the target of the listener request button to change state.
	 *
	 */
	private class ButtonListener implements ActionListener {
		/**
         * Invoked when an action occurs.
         * @param action - the action when the button is clicked 
         */
		public void actionPerformed( ActionEvent action) {
			  
			  JButton clickedButton = (JButton) action.getSource();
			  
			  if(clickedButton.equals(addButton)){
				  AddBikePanel addBikeScreen = new AddBikePanel(mainFrame);
				  mainFrame.setContentPane(addBikeScreen);
				  mainFrame.revalidate();
			  } else if (clickedButton.equals(backButton)) {
				  MainMenuPanel mainMenuScreen = new MainMenuPanel(mainFrame);
				  mainFrame.setContentPane(mainMenuScreen);
				  mainFrame.revalidate();
			  } else if (clickedButton.equals(deleteButton)) {
				  Bike bike = (Bike) bikeList.getSelectedValue();
				  
				  int response = JOptionPane.showConfirmDialog(null, 
						  "Are you sure you want to delete this bike?", "Warning!", 
						  JOptionPane.YES_NO_OPTION);
				  
				  if(response == JOptionPane.YES_OPTION) {
					  int deleteBike = bikeDAO.deleteBike(bike);
					  if (deleteBike == 0) {
						  URL okIconUrl = getClass().getResource(OK_ICON_URL);
						  ImageIcon okIcon = new ImageIcon(okIconUrl);
						  JOptionPane.showMessageDialog(mainFrame,
					  				"The bike was successfully deleted.",
					  				"Bike deleted!",
					  				JOptionPane.INFORMATION_MESSAGE, okIcon);
						  listModel.removeElement(bike);
					  } else {
						  JOptionPane.showMessageDialog(mainFrame,
					  				"Something went wrong!\nThe bike was not deleted.",
					  				"Error!", JOptionPane.WARNING_MESSAGE);
						  if (deleteBike == 2) {
							  listModel.removeElement(bike);
						  }
					  }
				  }	   
			  } else if (clickedButton.equals(typesButton)) {
				  TypePanel typeScreen = new TypePanel(mainFrame);
				  mainFrame.setContentPane(typeScreen);
				  mainFrame.revalidate();
			  }
		  }
	  }
	  
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
					BikePanel bikeScreen = new BikePanel(frame);
					frame.setContentPane(bikeScreen);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
