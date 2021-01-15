package main.java.com.team15.ebrs.view;

import java.awt.*;

import main.java.com.team15.ebrs.data.BikeType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import main.java.com.team15.ebrs.dao.TypeDAO;
import main.java.com.team15.ebrs.util.JTextFieldLimit;

/**
 *EditTypePanel is a container class with frame with the specified layout showing a window for managing the type of bikes.
 * @author Team 15
 */
public class EditTypePanel extends JPanel{
    
    private JFrame mainFrame = new MainFrame();
    private JTextField priceField;
    private JTextField nameField;
    private final TypeDAO tDAO = new TypeDAO();
    private BikeType type;
    private final int typeId;
    private double price;
    private String priceString;
    private String name;
    
    /**
     * Constructor for the EditType GUI. Will display the screen where you can edit the selected <code>Type</code>-object.
     * 
     * @param typeId The typeId of the Type object currently being edited.
     * @param frame The frame used in the entire GUI.
     * @see MainFrame
     * @see TypeDAO
     */

    public EditTypePanel(int typeId, JFrame frame) {
        type = tDAO.getType(typeId);
        type.setTypeId(typeId);
        this.typeId = typeId;
        name = type.getTypeName();
        price = type.getRentalPrice();
        mainFrame = frame;
        initialize();
    }

    /**
     *Initialize the contents of the frame.
     */
    private void initialize() {
        setLayout(null);
       /* ENTER PRICE HERE */
        priceField = new JTextField();
        priceField.setBounds(467, 300, 95, 28);
        priceString = Double.toString(price);
        priceField.setBorder(new LineBorder(Color.GRAY, 2, false));
        add(priceField);
        priceField.setColumns(10);
        priceField.setDocument(new JTextFieldLimit(15));
        priceField.setText(priceString);
        
        nameField = new JTextField();
        nameField.setBounds(467, 200, 95, 28);
        nameField.setBorder(new LineBorder(Color.GRAY, 2, false));
        add(nameField);
        nameField.setColumns(10);
        nameField.setDocument(new JTextFieldLimit(20));
        nameField.setText(name);
        
        //SAVE BUTTON
        JButton btnSave = new JButton("");
        btnSave.setBounds(430, 380, 150, 40);
        add(btnSave);
        btnSave.addActionListener(e -> saveButtonAction());
        btnSave.setOpaque(false);
        btnSave.setContentAreaFilled(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        
        //BACK BUTTON
        JButton btnBack = new JButton("");
        btnBack.setBounds(35, 0, 66, 66);
        add(btnBack);
        btnBack.addActionListener(e -> backButtonAction());
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setOpaque(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //Background
        ImageIcon types = new ImageIcon(getClass().getResource("/main/resources/com/team15/ebrs/images/EditType.png"));
        JLabel label = new JLabel("");
        label.setBounds(0, 0, 960, 540);
        label.setIcon(types);
	add(label); 
        
       
    }
    private void saveButtonAction(){
        try {
            String price1 = priceField.getText();
            name = nameField.getText();
            System.out.println(name + ". Price: " + price1);

            price = Double.parseDouble(price1);

            type = new BikeType(name, price);
            type.setTypeId(typeId);
            if(tDAO.editType(type) == 0){
                URL imgUrl = getClass().getResource("/main/resources/com/team15/ebrs/images/accept_icon.png");
                ImageIcon image1 = new ImageIcon(imgUrl);
                JOptionPane.showMessageDialog(mainFrame, "The type has been edited", "Type edited", JOptionPane.INFORMATION_MESSAGE, image1);

                TypePanel tp = new TypePanel(mainFrame);
                mainFrame.setContentPane(tp);
                mainFrame.revalidate();
            }else{
                JOptionPane.showMessageDialog(mainFrame, "Something went wrong", "Error", JOptionPane.WARNING_MESSAGE);
            }
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
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.add(new EditTypePanel(2, frame));
                    frame.setVisible(true);
                }catch (Exception e) {
                    e.printStackTrace();
                }
           }
        });
    }
}