

package main.java.com.team15.ebrs.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import main.java.com.team15.ebrs.dao.TypeDAO;
import main.java.com.team15.ebrs.data.BikeType;
import main.java.com.team15.ebrs.util.JTextFieldLimit;

/**
 * AddTypePanel is a container class with frame with the specified layout for adding a new type in database
 * @author Team 15
 *
 */

public class AddTypePanel extends JPanel{
    
    private final JFrame mainFrame;
    private JTextField priceField;
    private JTextField nameField;
    private final TypeDAO tDAO = new TypeDAO();
    private BikeType type;
    
 /**
  * The frame to be added to the panel
     * @param frame
  */
    public AddTypePanel(JFrame frame) {
        mainFrame = frame;
        initialize();
    }

 /**
  * Initialize the contents of the frame.
  */
    private void initialize() {
        setLayout(null);
       /* ENTER PRICE HERE */
        priceField = new JTextField();
        priceField.setBounds(467, 300, 95, 28);
        priceField.setBorder(new LineBorder(Color.GRAY, 2, false));
        add(priceField);
        priceField.setColumns(10);
        priceField.setDocument(new JTextFieldLimit(15));
        
        nameField = new JTextField();
        nameField.setBounds(467, 200, 95, 28);
        nameField.setBorder(new LineBorder(Color.GRAY, 2, false));
        add(nameField);
        nameField.setColumns(10);
        nameField.setDocument(new JTextFieldLimit(20));
        
        /* Button Back */
        JButton btnBack = new JButton("");
        btnBack.setBounds(35, 0, 66, 66);
        add(btnBack);
        btnBack.addActionListener(e -> backButtonAction());
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setOpaque(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //Save button
        JButton btnSave = new JButton("");
        btnSave.setBounds(430, 380, 150, 40);
        add(btnSave);
        btnSave.addActionListener(e -> saveButtonAction());
        btnSave.setContentAreaFilled(false);
        btnSave.setBorderPainted(false);
        btnSave.setOpaque(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

       
        //Picture
        ImageIcon types = new ImageIcon(getClass().getResource("/main/resources/com/team15/ebrs/images/NewType.png"));
        JLabel label = new JLabel("");
        label.setBounds(0, 0, 960, 540);
        label.setIcon(types);
	add(label);
        
        mainFrame.getRootPane().setDefaultButton(btnSave);//Allows for enter-press to save
        
       
    }
    
    private void saveButtonAction(){
        try {
            String price1 = priceField.getText();
            String name = nameField.getText();
            System.out.println(name + ". Price: " + price1);

            double price = Double.parseDouble(price1);

            BikeType type = new BikeType(name, price);
            tDAO.addType(type);

            URL imgUrl = getClass().getResource("/main/resources/com/team15/ebrs/images/accept_icon.png");
            ImageIcon image1 = new ImageIcon(imgUrl);
            JOptionPane.showMessageDialog(mainFrame, "The type has been added", "Type added", JOptionPane.INFORMATION_MESSAGE, image1);
            TypePanel tp = new TypePanel(mainFrame);
            mainFrame.setContentPane(tp);
            mainFrame.revalidate();

        }catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"Please enter a valid price");
        }
    }
    
    private void backButtonAction(){
        System.out.println("BACK");
        TypePanel typeScreen = new TypePanel(mainFrame);
        mainFrame.setContentPane(typeScreen);
        mainFrame.revalidate();
    }

    /**
     *  Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.add(new AddTypePanel(frame));
                    frame.setVisible(true);
                }catch (Exception e) {
                    e.printStackTrace();
                }
           }
        });
    }
}
