package main.java.com.team15.ebrs.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Class used to make lists used in the application transparent.
 * @author Team 15.
 */
public class TransparentListCellRenderer extends DefaultListCellRenderer {
    /**
     * 
     * Return a component that has been configured to display the specified value.
     *
     * @param list         The JList we're painting.
     * @param value        The value returned by list.getModel().getElementAt(index).
     * @param index        The cells index.
     * @param isSelected   True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     */
    
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        /* HEX KODE TO COLOR */
        /* COLOR OF FONT */
        //setForeground(Color.BLACK);
        setOpaque(isSelected);
        return this;
    }
}
