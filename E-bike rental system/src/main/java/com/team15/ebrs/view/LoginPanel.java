package main.java.com.team15.ebrs.view;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.codec.binary.Base64;

import main.java.com.team15.ebrs.dao.UserDAO;
import main.java.com.team15.ebrs.data.User;
import main.java.com.team15.ebrs.util.Password;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.URL;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {

	private JFrame mainFrame;
	private JLabel background;
	private JLabel failedLogin;
	private JButton loginButton;
	private JTextField emailField;
	private JPasswordField passwordField;
	private UserDAO userDAO = new UserDAO();
	
	private static final String BG_IMAGE_URL = "/main/resources/com/team15/ebrs/images/login.png";
	private static final String LOGIN_FAILED = "Login failed. Wrong email or password.";
	private static final String NO_MAIL_OR_PSWD = "Please enter both email and password.";

	/**
	 * Create the JPanel.
	 */
	public LoginPanel(JFrame frame) {
		mainFrame = frame;
		initialize();
	}

	/**
	 * Initialize the contents of the panel.
	 */
	private void initialize() {
		setLayout(null);
		
		/* TextField for Email */
		emailField = new JTextField();
		emailField.setFont(new Font("Arial", Font.BOLD, 12));
		emailField.setBounds(385, 217, 172, 20);
		emailField.setBorder(javax.swing.BorderFactory.createEmptyBorder()); // Remove the white border
		emailField.setOpaque(false);
		add(emailField);
		
		/* PasswordField */
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Arial", Font.BOLD, 12));
		passwordField.setBounds(385, 285, 172, 20);
		passwordField.setBorder(javax.swing.BorderFactory.createEmptyBorder()); // Remove the white border
		passwordField.setOpaque(false);
		add(passwordField);
		
		/* JLabel with info if login fails */
		failedLogin = new JLabel();
		failedLogin.setBounds(386, 285, 300, 100);
		failedLogin.setForeground(Color.RED);
		failedLogin.setFont(new Font("Tahoma", Font.PLAIN, 11));
		add(failedLogin);
		
		/* Login Button */
		loginButton = new JButton();
		loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!(emailField.getText().equals("") || passwordField.getPassword().length == 0)) {
					User user = userDAO.getUser(emailField.getText());
					if (user != null) {
						byte[] salt = Base64.decodeBase64(user.getUserSalt());
						char[] password = passwordField.getPassword();
						byte[] hashedPassword = Base64.decodeBase64(user.getUserPassword());
						if (Password.isExpectedPassword(password, salt, hashedPassword)) {
							MainMenuPanel mainMenuScreen = new MainMenuPanel(mainFrame);
							mainFrame.setContentPane(mainMenuScreen);
							mainFrame.revalidate();
						}
					}
					failedLogin.setText(LOGIN_FAILED);
				} else {
					failedLogin.setText(NO_MAIL_OR_PSWD);
				}
			}
		});
		loginButton.setBounds(391, 374, 168, 34);
		add(loginButton);
		
		/* Make the button transparent */
		loginButton.setOpaque(false);
		loginButton.setContentAreaFilled(false);
		loginButton.setBorderPainted(false);
		
		/* Set the button as default button */
		mainFrame.getRootPane().setDefaultButton(loginButton);
		
		/* Load the background */
		URL bgUrl = getClass().getResource(BG_IMAGE_URL);
		ImageIcon bgImage = new ImageIcon(bgUrl);
		
		background = new JLabel();
		background.setBounds(0, 0, 960, 540);
		background.setIcon(bgImage);
		add(background);
	}
	
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
