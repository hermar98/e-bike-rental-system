

package main.java.com.team15.ebrs.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import main.java.com.team15.ebrs.dao.TypeDAO;
import main.java.com.team15.ebrs.data.BikeType;

/**
 * TypePanel is a container class with frame with the specified layout for managing the types of bikes
 * @author Team 15
 *
 */


public class TypePanel extends JPanel{
    
    private int typeId;
    private final JFrame mainFrame;
    private final TypeDAO tDAO = new TypeDAO();
    private ArrayList<BikeType> arrayType = new ArrayList<>();
    private final DefaultListModel typelist = new DefaultListModel();
    private JList listOfTypes;

    /**
     *The frame to be added to the panel
     * @param frame
     */
    public TypePanel(JFrame frame) {
        arrayType = tDAO.getAllTypes();
        this.typeId = 0;
        mainFrame = frame;
        initialize();
    }

 /**
  * Initialize the contents of the frame.
  */
    private void initialize() {
        setLayout(null);

       
        //Headline
        /*JLabel lblNewType = new JLabel("ALL TYPES");
        lblNewType.setFont(new Font("Tekton Pro", Font.PLAIN, 30));
        lblNewType.setBounds(400, 10, 298, 76);
        lblNewType.setForeground(Color.white);
        add(lblNewType);
        */
        for(BikeType t : arrayType){
            String typeOutput = "ID " + t.getTypeId() + " - " + t.getTypeName() + " - " + String.format("%.2f", t.getRentalPrice()) + "kr";
            typelist.addElement(typeOutput);
        }
        
        listOfTypes = new JList(typelist);
        listOfTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listOfTypes.setFont(new Font("Tekton Pro", Font.BOLD, 20));
        listOfTypes.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        listOfTypes.setOpaque(false);
        listOfTypes.setCellRenderer(new TransparentListCellRenderer());
        listOfTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        listOfTypes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                String bikeinfo = (String)listOfTypes.getModel().getElementAt(listOfTypes.locationToIndex(evt.getPoint()));
                String[] bikeid1 = bikeinfo.split(" ");
                
                typeId = Integer.parseInt(bikeid1[1]);
                System.out.println(typeId);
                if (evt.getClickCount() == 2) {
                    System.out.println("Double click");
                    EditTypePanel edit = new EditTypePanel(typeId, mainFrame);
                    mainFrame.setContentPane(edit);
                    mainFrame.revalidate();
                }
            }
        });
        
        JScrollPane sp = new JScrollPane(listOfTypes);
        sp.setBounds(350, 70, 400, 400);
        sp.setBackground(Color.GRAY);
        sp.getViewport().setOpaque(false);
        sp.setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
        sp.setOpaque(false);
        sp.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        add(sp);
        
        JButton btnBack = new JButton("");
        btnBack.setBounds(15, 0, 70, 70);
        btnBack.addActionListener(e -> backButtonAction());
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setOpaque(false);
        add(btnBack);


        //Add and delete buttons
        JButton addB = new JButton("");
        addB.setBounds(780, 315, 150, 60);
        addB.addActionListener(e -> addButtonAction());
        addB.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addB.setContentAreaFilled(false);
        addB.setBorderPainted(false);
        addB.setOpaque(false);
        add(addB);

        JButton delB = new JButton("");
        delB.setBounds(780, 390, 150, 60);
        delB.addActionListener(e -> deleteButtonAction());
        delB.setCursor(new Cursor(Cursor.HAND_CURSOR));
        delB.setContentAreaFilled(false);
        delB.setBorderPainted(false);
        delB.setOpaque(false);
        add(delB);
        
        
        //Background
        ImageIcon types = new ImageIcon(getClass().getResource("/main/resources/com/team15/ebrs/images/Alltypes.png"));
        JLabel label = new JLabel("");
        label.setBounds(0, 0, 960, 540);
        label.setIcon(types);
	add(label);
    
    }
    
    private void addButtonAction(){
        System.out.println("Add");
        AddTypePanel add = new AddTypePanel(mainFrame);
        mainFrame.setContentPane(add);
        mainFrame.revalidate();
    }
    
    private void backButtonAction(){
        System.out.println("BACK");
        BikePanel BikePanel = new BikePanel(mainFrame);
        mainFrame.setContentPane(BikePanel);
        mainFrame.revalidate();
    }
    
    private void deleteButtonAction(){
        System.out.println("Delete");
        if(listOfTypes.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a type to be deleted" );
        }
        else {
            int chosenIndex = listOfTypes.getSelectedIndex();
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this type?\nAll bikes registered as this type will be deleted.", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(response == JOptionPane.YES_OPTION) {

                if(tDAO.deleteType(typeId) == 0){
                    typelist.remove(chosenIndex);
                }else{
                    System.out.println("Something went wrong when deleting the selected type");
                }
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
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            /* HEX KODE TO COLOR */
            
            /* COLOR OF FONT */
            setForeground(Color.black);
            setOpaque(isSelected);
            return this;
        }
    }
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.add(new TypePanel(frame));
                    frame.setVisible(true);
                }catch (Exception e) {
                    e.printStackTrace();
                }
           }
        });
    }
}

