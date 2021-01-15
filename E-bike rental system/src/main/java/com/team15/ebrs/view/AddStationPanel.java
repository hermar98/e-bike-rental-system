package main.java.com.team15.ebrs.view;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JTextPane;

import main.java.com.team15.ebrs.data.DockingStation;
import main.java.com.team15.ebrs.dao.DockingStationDAO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

/**
 * AddStationPanel is a container class with frame with the specified layout for adding a new docking station in database
 * @author Team 15
 *
 */
public class AddStationPanel extends JPanel {

	private DockingStationDAO dsDAO = new DockingStationDAO();
	
	private JFrame mainFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//AddDockingStation window = new AddDockingStation();
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * The frame to be added to the panel
	 * @param frame
	 */
	public AddStationPanel(JFrame frame) {
		mainFrame = frame;
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setLayout(null);
		
		JTextPane txtName = new JTextPane();
		txtName.setFont(new Font("YuKyokasho Yoko", Font.BOLD, 17));
		txtName.setText("");
		txtName.setBounds(445, 112, 277, 42);
		add(txtName);
		txtName.setOpaque(false);
		
		JTextPane txtCoordinatelat = new JTextPane();
		txtCoordinatelat.setText("");
		txtCoordinatelat.setFont(new Font("YuKyokasho Yoko", Font.BOLD, 17));
		txtCoordinatelat.setBounds(450, 203, 277, 42);
		add(txtCoordinatelat);
		txtCoordinatelat.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isDigit(c)&& c!=127&& c!=8 && c!='.') {
					JOptionPane.showMessageDialog(null, "Please enter the number", "Warning",JOptionPane.WARNING_MESSAGE);  
					e.consume();
				}
			}
		});
		txtCoordinatelat.setOpaque(false);
		
		JTextPane txtCoordinatelng = new JTextPane();
		txtCoordinatelng.setText("");
		txtCoordinatelng.setFont(new Font("YuKyokasho Yoko", Font.BOLD, 17));
		txtCoordinatelng.setBounds(450, 304, 277, 42);
		add(txtCoordinatelng);
		txtCoordinatelng.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isDigit(c)&& c!=127&& c!=8 && c!='.') {
					JOptionPane.showMessageDialog(null, "Please enter the number", "Warning",JOptionPane.WARNING_MESSAGE);  
					e.consume();
				}
			}
		});
		txtCoordinatelng.setOpaque(false);
		
		JTextPane txtTotaldocks = new JTextPane();
		txtTotaldocks.setText("");
		txtTotaldocks.setFont(new Font("YuKyokasho Yoko", Font.BOLD, 17));
		txtTotaldocks.setBounds(450, 393, 277, 42);
		add(txtTotaldocks);
		txtTotaldocks.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!Character.isDigit(c)&& c!=127&& c!=8) {
					JOptionPane.showMessageDialog(null, "The number of the docks can only be a whole number", "Warning",JOptionPane.WARNING_MESSAGE);  
					e.consume();
				}
			}
		});
		txtTotaldocks.setOpaque(false);
		
		/* Background pic */
		ImageIcon background = new ImageIcon(getClass().getResource("/main/resources/com/team15/ebrs/images/add.png"));
		JLabel lblbackground = new JLabel(background);
		lblbackground.setBounds(0, 0, 960, 518);
		add(lblbackground);
		
		
		JButton btnNext = new JButton("Save");
		btnNext.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtName.getText().equals("")||txtCoordinatelat.getText().equals("")||txtCoordinatelng.getText().equals("")||txtTotaldocks.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "The fields cannot be empty.", "Information",JOptionPane.WARNING_MESSAGE);  
				}else{
					String stationName   = txtName.getText().trim();
					double coordinatelat = Double.parseDouble(txtCoordinatelat.getText().trim());
					double coordinatelng = Double.parseDouble(txtCoordinatelng.getText().trim());
					int total_docks = Integer.parseInt(txtTotaldocks.getText().trim());
					try {
						DockingStation newStation = new DockingStation(stationName,coordinatelat,coordinatelng,total_docks);
						if(dsDAO.addDockingStation(newStation)) {
							JOptionPane.showMessageDialog(null, "The new station is successfully registered", "Information",JOptionPane.WARNING_MESSAGE);  
						}else {
							JOptionPane.showMessageDialog(null, "Error! The station name is already registered \nPlease try another name", "Information",JOptionPane.WARNING_MESSAGE); 
						}
					}catch(Exception nullPointerException ) {
						JOptionPane.showMessageDialog(null, "Please fill out the information");
					}
				}
			}
		});
		btnNext.setBounds(758, 419, 145, 93);
		add(btnNext);
		btnNext.setOpaque(false);

		mainFrame.getRootPane().setDefaultButton(btnNext);

		JButton btnBack = new JButton("Back");
		btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StationPanel stationScreen = new StationPanel(mainFrame);
				mainFrame.setContentPane(stationScreen);
				mainFrame.revalidate();
			}
		});
		btnBack.setBounds(0, -13, 75, 76);
		add(btnBack);
		btnBack.setOpaque(false);

	}
}
