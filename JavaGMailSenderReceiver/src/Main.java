import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.net.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    private ServerSocket serv;
    private Socket mySocket;
    private String username;
    private String mdp;
    private String dest;
    private String atm;
    private String addrSender;
    Scanner scan = new Scanner(System.in);

    public Main() {

        try {
            InetAddress inetAddress = InetAddress.getByName("localhost");
            SocketAddress endPoint = new InetSocketAddress(inetAddress, 15500);
            serv.bind(endPoint);
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.print("Entrez votre adresse gmail: ");
        username = scan.next();
        //username = "louis.blenner@gmail.com";
        System.out.print("Entrez votre mot de passe application : ");
        mdp = scan.next();
        System.out.print("Entrez l'adresse de l'envoyeur : ");
        addrSender = scan.next();
        //addrSender = "louis.blenner@gmail.com";

    }

    public void routine() {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        while (true) {

            try {
                mySocket = serv.accept();
                InputStream in = mySocket.getInputStream();
                byte[] atmTab = in.readAllBytes();
                atm = atmTab.toString();
            }catch(Exception e){
                e.printStackTrace();
            }

            SendMail sender = new SendMail();
            sender.send(username, mdp, dest, "Sujet", "", atm);

            System.out.println("Success");

        }
    }


    public static void main(String[] args) {

        Main go = new Main();
        go.routine();

    }

}
