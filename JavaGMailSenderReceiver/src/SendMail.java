import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Scanner;

public class SendMail {

    Properties prop;
    public SendMail(){
        prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
    }

    public void send(String username, String mdp, String dest, String sujet, String msg, String atcm){

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



            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            File file = new File("Attach");

            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(atcm); //Ici une string
            //FileOutputStream writer = new FileOutputStream("Attach");
            //writer.write(atcm);
            writer.close();

            messageBodyPart.setFileName("titre");
            messageBodyPart.attachFile(file);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            
            Transport.send(message);

        } catch (Exception e) {
            System.out.println("Echec de l'envoie");
        }
    }

    public static void main (String[] args) {

        Scanner scan = new Scanner(System.in);
        //Parametre pour l'envoie de Mail
        //System.out.print("Entrez votre adresse gmail: ");
        String username = "louis.blenner@gmail.com"; //scan.next();
        System.out.print("Entrez votre mot de passe application : ");
        String mdp = scan.next();
        //System.out.print("Entrez l'adresse de la cible : ");
        String dest = "louis.blenner@gmail.com"; //scan.next();

        SendMail sender = new SendMail();
        sender.send(username, mdp, dest, "Astre", "", "hey");
    }


}
