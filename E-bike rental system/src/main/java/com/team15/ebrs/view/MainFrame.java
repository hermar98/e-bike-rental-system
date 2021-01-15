package main.java.com.team15.ebrs.view;

import java.awt.Container;
import java.awt.EventQueue;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The MainFrame class that extends JFrame. Used as the Frame for the entire application.
 * @author Team 15
 */
public class MainFrame extends JFrame {
	private static final int FRAME_WIDTH = 960;
	private static final int FRAME_HEIGHT = 540;
	private static final String FRAME_TITLE = "E-bike rental system";
	private static final String ICON_URL = "/main/resources/com/team15/ebrs/images/main_icon.png";

	/**
	 * Constructor for the MainFrame. This frame is used for the entire application, only changing the active panel being displayed.
	 */
	public MainFrame() {
		URL iconUrl = getClass().getResource(ICON_URL);
		ImageIcon frameIcon = new ImageIcon(iconUrl);
		
		setTitle(FRAME_TITLE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(frameIcon.getImage());
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					LoginPanel loginScreen = new LoginPanel(frame);
					frame.setContentPane(loginScreen);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
