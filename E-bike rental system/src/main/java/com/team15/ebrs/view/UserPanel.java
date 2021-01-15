package main.java.com.team15.ebrs.view;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import org.apache.commons.codec.binary.Base64;

import main.java.com.team15.ebrs.dao.UserDAO;
import main.java.com.team15.ebrs.data.User;
import main.java.com.team15.ebrs.util.MailSender;
import main.java.com.team15.ebrs.util.Password;
import main.java.com.team15.ebrs.util.TransparentListCellRenderer;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * UserPanel is a container class with frame with the specified layout for managing the users 
 *
 */
public class UserPanel extends JPanel {
	
	private DefaultListModel listModel;
	private JList userList;
	private JScrollPane scrollPane;
	
	private JTextField emailField;
	private JTextPane textPane;
	
	private JButton registerButton;
	private JButton editButton;
	private JButton deleteButton;
	private JButton backButton;
	private JLabel userNameLabel;
	private JLabel userEmailLabel;
	
	private UserDAO userDAO = new UserDAO();
	private ArrayList<User> dbUserList;
	
	private JFrame mainFrame;
	
	private static final String ENTER_EMAIL = "Please enter an email";
	
	private static final String BG_IMAGE_URL = "/main/resources/com/team15/ebrs/images/users.png";
	private static final String OK_ICON_URL = "/main/resources/com/team15/ebrs/images/accept_icon.png";

	/**
	 * Create the JPanel.
	 */
	public UserPanel(JFrame frame) {
		mainFrame = frame;
	    dbUserList = userDAO.getAllUsers();
		initialize();
	}

	/**
	 * Initialize the contents of the panel.
	 */
	private void initialize() {
		setLayout(null);

		/* Labels with info about the user */
		userNameLabel = new JLabel();
		userNameLabel.setBounds(650, 400, 250, 50);
		userNameLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
		userNameLabel.setForeground(Color.BLACK);
		add(userNameLabel);
		
		userEmailLabel = new JLabel();
		userEmailLabel.setBounds(650, 450, 250, 25);
		userEmailLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
		userEmailLabel.setForeground(Color.BLACK);
		add(userEmailLabel);
		
		/* Email Text Field */
		emailField = new JTextField();
		emailField.setBounds(86, 400, 180, 30);
		emailField.setText(ENTER_EMAIL);
		emailField.setFont(new Font("Verdana", Font.PLAIN, 13));
		emailField.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
            	if (emailField.getText().equals(ENTER_EMAIL)) {
	                emailField.setText("");
	                mainFrame.getRootPane().setDefaultButton(registerButton);
            	}
            }
		});
		add(emailField);
		
		/* Adding the buttons */
		registerButton = new JButton();
		registerButton.setBounds(270, 444, 67, 57);
		registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(registerButton);
		
		editButton = new JButton();
		editButton.setBounds(533, 393, 90, 52);
		editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(editButton);
		
		deleteButton = new JButton();
		deleteButton.setBounds(533, 453, 90, 47);
		deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(deleteButton);
		
		backButton = new JButton();
		backButton.setBounds(0, 0, 85, 85);
		backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(backButton);
		
		/* Adding ButtonListener-object to the buttons */
		ButtonListener bl = new ButtonListener();
		registerButton.addActionListener(bl);
		editButton.addActionListener(bl);
		deleteButton.addActionListener(bl);
		backButton.addActionListener(bl);
		
		/* Hide the buttons */
		hideButton(registerButton);
		hideButton(editButton);
		hideButton(deleteButton);
		hideButton(backButton);

		/* Load the user list */
        loadUserList();
        
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
			
			if (clickedButton.equals(registerButton) && !emailField.getText().equals(ENTER_EMAIL)) {
				String email = emailField.getText();
				
				if (MailSender.isValidEmail(email)) {
					JTextField firstNameField = new JTextField(10);
					JTextField lastNameField = new JTextField(10);
					JLabel registerInfo = new JLabel("Please enter the name of the new user: ");
					JLabel firstNameLabel = new JLabel("First name");
					JLabel lastNameLabel = new JLabel("Last name");
					
					firstNameField.setBounds(0, 45, 150, 20);
					lastNameField.setBounds(0, 90, 150, 20);
					registerInfo.setBounds(0, 0, 300, 20);
					firstNameLabel.setBounds(0, 25, 300, 20);
					lastNameLabel.setBounds(0, 70, 300, 20);
					
					JPanel namePanel = new JPanel();
					namePanel.setLayout(null);
				    namePanel.add(registerInfo);
				    namePanel.add(firstNameLabel);
				    namePanel.add(firstNameField);
				    namePanel.add(lastNameLabel);
				    namePanel.add(lastNameField);
				    
				    UIManager.put("OptionPane.minimumSize", new Dimension(300, 175));
				    int result = JOptionPane.showConfirmDialog(null, namePanel, 
				    		"Register new user", JOptionPane.OK_CANCEL_OPTION);
				    UIManager.put("OptionPane.minimumSize", null);
				    
				    if (result == JOptionPane.OK_OPTION && !firstNameField.getText().equals("") 
				    		&& !lastNameField.getText().equals("")) {
				    	String firstName = firstNameField.getText();
				    	String lastName = lastNameField.getText();
				    	String password = Password.generateRandomPassword(6);
				    	byte[] salt = Password.genNextSalt();
				    	byte[] hashedPassword = Password.hash(password.toCharArray(), salt);
				    	String hashedPwdString = Base64.encodeBase64String(hashedPassword);
				    	String saltString = Base64.encodeBase64String(salt);
				    	User user = new User(email, firstName, lastName, hashedPwdString, saltString);
				    	
				    	if (userDAO.addUser(user)) {
				    		if (MailSender.sendMail(email, password)) {
				    			URL okIconUrl = getClass().getResource(OK_ICON_URL);
				    			ImageIcon okIcon = new ImageIcon(okIconUrl);
				    			JOptionPane.showMessageDialog(mainFrame,
									    "The new user was successfully registered.",
									    "Register successful!",
									    JOptionPane.INFORMATION_MESSAGE, okIcon);
				    			listModel.addElement(user);
				    		}
				    	} else {
				    		JOptionPane.showMessageDialog(mainFrame,
								    "Registration failed!\nThe email is already registered in the system.",
								    "Error!",
								    JOptionPane.WARNING_MESSAGE);
				    	}
				    } else if (result == JOptionPane.OK_OPTION) {
				    	JOptionPane.showMessageDialog(mainFrame,
				    			"Please enter both first name and last name\nfor the user!",
							    "Error!",
							    JOptionPane.WARNING_MESSAGE);
				    }
				    
				} else {
					JOptionPane.showMessageDialog(mainFrame,
						    "Please enter a valid email adress!",
						    "Error!",
						    JOptionPane.WARNING_MESSAGE);
				}
				
   		  	} else if (clickedButton.equals(editButton)) {
	   		  	JLabel oldPassLabel = new JLabel("Current password:");
	   		  	JLabel newPassLabel = new JLabel("New password (min. 6 characters):");
	   		  	JLabel newPassLabel2 = new JLabel("New password again:");
	   		  	JPasswordField oldPassword = new JPasswordField();
	   		  	JPasswordField newPassword = new JPasswordField();
	   		  	JPasswordField newPassword2 = new JPasswordField();
	   		  	
	   		  	oldPassLabel.setBounds(0, 0, 300, 20);
	   		  	oldPassword.setBounds(0, 20, 150, 20);
	   		  	newPassLabel.setBounds(0, 45, 300, 20);
	   		  	newPassword.setBounds(0, 65, 150, 20);
	   		  	newPassLabel2.setBounds(0, 90, 300, 20);
	   		  	newPassword2.setBounds(0, 110, 150, 20);
	   		  	
	   		  	JPanel passPanel = new JPanel();
	   		  	passPanel.setLayout(null);
	   		  	passPanel.add(oldPassLabel);
	   		  	passPanel.add(oldPassword);
	   		  	passPanel.add(newPassLabel);
	   		  	passPanel.add(newPassword);
	   		  	passPanel.add(newPassLabel2);
	   		  	passPanel.add(newPassword2);
	   		  	
	   		  	UIManager.put("OptionPane.minimumSize", new Dimension(320, 200));
			    int result = JOptionPane.showConfirmDialog(null, passPanel, 
			    		"Change password", JOptionPane.OK_CANCEL_OPTION);
			    UIManager.put("OptionPane.minimumSize", null);
			    
			    if (result == JOptionPane.OK_OPTION) {
			    	User user = (User) userList.getSelectedValue();
			    	User dbUser = userDAO.getUser(user.getEmail());
			    	if (dbUser != null) {
			    		user.setUserPassword(dbUser.getUserPassword());
			    		user.setUserSalt(dbUser.getUserSalt());
			    	} else {
			    		JOptionPane.showMessageDialog(mainFrame,
				  				"Something went wrong!\nThe password was not changed.",
				  				"Error!",
				  				JOptionPane.WARNING_MESSAGE);
			    	}
			    	byte[] hashedPass = Base64.decodeBase64(user.getUserPassword());
			    	byte[] salt = Base64.decodeBase64(user.getUserSalt());
	   			  	if (oldPassword.getPassword().length == 0 || newPassword.getPassword().length == 0
	   			  			|| newPassword2.getPassword().length == 0) {
	   			  		JOptionPane.showMessageDialog(mainFrame,
	   			  				"Please fill out all the password fields.",
	   			  				"Error!",
	   			  				JOptionPane.WARNING_MESSAGE);
	   			  	} else if (!Arrays.equals(newPassword.getPassword(), newPassword2.getPassword())) {
	   			  		JOptionPane.showMessageDialog(mainFrame,
				  				"The passwords entered do not match,\nplease try again.",
				  				"Error!",
				  				JOptionPane.WARNING_MESSAGE);
	   			  	} else if (newPassword.getPassword().length < 6) {
	   			  	JOptionPane.showMessageDialog(mainFrame,
			  				"The new password needs to be at least 6 characters long.",
			  				"Error!",
			  				JOptionPane.WARNING_MESSAGE);
	   			  	} else if (Password.isExpectedPassword(oldPassword.getPassword(), salt, hashedPass)) {
	   			  		byte[] newSalt = Password.genNextSalt();
	   			  		byte[] newHashedPass = Password.hash(newPassword.getPassword(), newSalt);
	   			  		User editedUser = new User(user.getEmail(), user.getFirstname(), user.getSurname(),
	   			  				Base64.encodeBase64String(newHashedPass), Base64.encodeBase64String(newSalt));
	   			  		if (userDAO.editUser(editedUser)) {
		   			  		URL okIconUrl = getClass().getResource(OK_ICON_URL);
		   			  		ImageIcon okIcon = new ImageIcon(okIconUrl);
		   			  		JOptionPane.showMessageDialog(mainFrame,
		   			  				"The password for '" + user.getEmail() + "'\nwas successfully changed.",
		   			  				"Password changed!",
		   			  				JOptionPane.INFORMATION_MESSAGE, okIcon);
	   			  		} else {
	   			  		JOptionPane.showMessageDialog(mainFrame,
				  				"Something went wrong!\nThe password was not changed.",
				  				"Error!",
				  				JOptionPane.WARNING_MESSAGE);
	   			  		}
	   			  	} else {
	   			  	JOptionPane.showMessageDialog(mainFrame,
			  				"The current password was not correct,\nplease try again.",
			  				"Error!",
			  				JOptionPane.WARNING_MESSAGE);
	   			  	}
			    }
   		  	} else if (clickedButton.equals(deleteButton)) {
   		  		if (listModel.getSize() > 1) {
	   		  		User user = (User) userList.getSelectedValue();
	   		  		int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the user\n'"
	   		  				+ user.getEmail() + "'?", "Warning!", JOptionPane.YES_NO_OPTION);
	   		  		if (response == JOptionPane.YES_OPTION) {
	   		  			if (userDAO.deleteUser(user)) {
	   		  				URL okIconUrl = getClass().getResource(OK_ICON_URL);
	   		  				ImageIcon okIcon = new ImageIcon(okIconUrl);
	   		  				JOptionPane.showMessageDialog(mainFrame,
	   		  						"The user was successfully deleted.",
	   		  						"User deleted!",
	   		  						JOptionPane.INFORMATION_MESSAGE, okIcon);
	   		  				listModel.removeElement(user);
	   		  				userList.setSelectedIndex(0);
	   		  				User selectedUser = (User) userList.getSelectedValue();
	   		  				if (user != null) {
	   		  					userNameLabel.setText("Name: " + selectedUser.getFirstname() + " " + selectedUser.getSurname());
	   		  					userEmailLabel.setText("Email: " + selectedUser.getEmail());
	   		  				}
	   		  			} else {
	   		  				JOptionPane.showMessageDialog(mainFrame,
	   		  						"Something went wrong!\nThe user was not deleted.",
	   		  						"Error!",
	   		  						JOptionPane.WARNING_MESSAGE);
	   		  			}
	   		  		}
   		  		} else {
   		  		JOptionPane.showMessageDialog(mainFrame,
	  						"You can not delete the last user in the system!",
	  						"Error!",
	  						JOptionPane.WARNING_MESSAGE);
   		  		}
   		  		
   		  	} else if (clickedButton.equals(backButton)) {
   		  		MainMenuPanel mainMenuScreen = new MainMenuPanel(mainFrame);
   		  		mainFrame.setContentPane(mainMenuScreen);
   		  		mainFrame.revalidate();
   		  	}
		}
	}

    /**
     * display all the available users on the Jlist.
     */
	private void loadUserList() {
		listModel = new DefaultListModel();
		for (User user: dbUserList) {
            listModel.addElement(user);
        }
		
        userList = new JList(listModel);
        userList.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
		userList.setOpaque(false);
		userList.setCellRenderer(new TransparentListCellRenderer());
        userList.setSelectedIndex(0);
        
        User user = (User) userList.getSelectedValue();
        if (user != null) {
	        userNameLabel.setText("Name: " + user.getFirstname() + " " + user.getSurname());
			userEmailLabel.setText("Email: " + user.getEmail());
        }

        userList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	JList list = (JList) e.getSource();
        		User user = (User) list.getSelectedValue();
        		userNameLabel.setText("Name: " + user.getFirstname() + " " + user.getSurname());
        		userEmailLabel.setText("Email: " + user.getEmail());
            }
        });
		
		scrollPane = new JScrollPane(userList);
		scrollPane.setBounds(640, 43, 275, 320);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

		add(scrollPane);
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
					UserPanel userScreen = new UserPanel(frame);
					frame.setContentPane(userScreen);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
