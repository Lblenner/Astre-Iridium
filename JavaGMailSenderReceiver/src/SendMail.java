import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SendMail {

    Properties prop;
    public SendMail(){
        prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
    }

    public void send(String username, String mdp,String dest,String sujet,String msg,String atcm){

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, mdp);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(dest)
            );
            message.setSubject(sujet);
            message.setText(msg);
            
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            Multipart multipart = new MimeMultipart();

            messageBodyPart = new MimeBodyPart();
            String file = atcm;
            String[] fileStrs = file.split("\\\\");            
            String fileName = fileStrs[fileStrs.length-1];
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            System.out.println("Sending");
            
            Transport.send(message);

            System.out.println("Success");

        } catch (AuthenticationFailedException e) {
            System.out.println("Authentification échoué");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
