package codesnippets;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * @author UEX13061
 * @date 24.07.2023
 * @necessaryDependencies
 *
 *         <dependency>
 *             <groupId>com.sun.mail</groupId>
 *             <artifactId>jakarta.mail</artifactId>
 *             <version><NEWEST VERSION></version>
 *         </dependency>
 *         <dependency>
 *             <groupId>jakarta.activation</groupId>
 *             <artifactId>jakarta.activation-api</artifactId>
 *             <version><NEWEST VERSION></version>
 *         </dependency>
 */
public class MailClient {
    public static void main(String[] args) {

        // Recipient's email address
        String to = "";
        String cc = "";

        // Sender's email address
        String from = "noreply-apollon@raiffeisen.ch";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", "");
        properties.put("mail.smtp.port", "");

        // Get the Session object and pass username and password if necessary
        Session session = Session.getInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set CC: header field of the header.
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));

            // Set Subject: header field
            message.setSubject("Hi! This is a Mail From Apollon");

            // Set the actual message
            message.setText("Ciao Eliseo, Check the Java Code :-) for multiple user or users in CC");

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
