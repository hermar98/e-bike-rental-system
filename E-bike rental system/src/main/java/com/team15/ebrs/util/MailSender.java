
package main.java.com.team15.ebrs.util;

import main.java.com.team15.ebrs.view.UserPanel;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class is used to send automated mails.
 * @author Team15
 */
public class MailSender {
    /**
     * Used to send automated passwords to new users registered to the system. Sends via team15s official Gmail account.
     * @param recipientAddress The email address for the recipient.
     * @param autoPassword The automated password being sent, used to login to the system.
     * @return Boolean stating whether the email was sent successfully.
     */
    public static boolean sendMail(String recipientAddress, String autoPassword){  
        final String username = "donotreply.Team15@gmail.com";
        final String password = "Team15mail";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                }
          });

        try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("DoNotReply.Team15@gmail.com"));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipientAddress));
                message.setSubject("Account created for EBRS");
                message.setText("Hello! \n\nAn account for Electrical Bike Rental System Ltd. has been created.\nLogin using your email address and the following auto-generated password: " + autoPassword + "\nIf you haven't requested this account, please disregard this message.\n\nSincerely yours,\nTeam 15 at Electrical Bike Rental System Ltd.\n\n---------------------------------------------\n This is an automated message, any responses towards this email will not be read or responded.");

                Transport.send(message);               
                return true;
                
        } catch (MessagingException e) {
            return false;
        }
    }

    /**
     * Used in UserPanel when registering new admin-account. Simple check validating if the entered email has the "standard" layout for an email.
     * @param email The email being checked for validity
     * @see UserPanel
     * @return Boolean stating whether the specified email is valid according to this simple check.
     */
    public static boolean isValidEmail(String email) {
    	String[] splittedEmail = email.split("@|\\.");
    	if (splittedEmail.length == 3) {
    		return true;
    	}
    	return false;
    }
}

