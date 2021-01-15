
package main.java.com.team15.ebrs.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Used to set a charater limit for JTextFields. Used in several places in the view to make sure users of the system can't enter text larger than max value set in database coloumn.
 * 
 */
public class JTextFieldLimit extends PlainDocument {
    private final int limit;

    /**
     * Constructor used to set the limit for the desired textField. Used inside JTextField.setDocument().
     * @param limit The desired character limit.
     */
    public JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    /**
     * Overrides the insertString method used when entering text in a JTextField
     */
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null){
        return;
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
      }
    }
}
