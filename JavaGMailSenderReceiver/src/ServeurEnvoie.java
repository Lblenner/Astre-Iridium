import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ServeurEnvoie {

    private ServerSocket serv;
    private Socket mySocket;
    Scanner scan = new Scanner(System.in);

    private String username;
    private String mdp;
    private String dest;
    private String atm;

    private InputStream in;


    public ServeurEnvoie() {

        //Connection au port de localhost 15500 de la ServerSocket
        try {
            mySocket = new Socket(InetAddress.getLocalHost(),15500);
            in = mySocket.getInputStream();
        } catch (Exception e){
            e.printStackTrace();
        }

        //Parametre pour l'envoie de Mail
        System.out.print("Entrez votre adresse gmail: ");
        username = "louis.blenner@gmail.com"; //scan.next();
        System.out.print("Entrez votre mot de passe application : ");
        mdp = scan.next();
        System.out.print("Entrez l'adresse de la cible : ");
        dest = "louis.blenner@gmail.com"; //scan.next();
        System.out.println("Serveur lancé.");

    }

    public void routine() {

        while(true) {
            //String msg = "";
            int len;
            byte[] tab = null;

            try {

                boolean msgLu = false;

                if ((len =in.available()) != 0) {
                    tab = new byte[len];
                    msgLu = true;
                    in.read(tab,0,len);
                    /*String hex =  Integer.toHexString(data);
                    if (hex.length() == 1){
                        hex = "0"+hex;
                    }
                    System.out.println("Bytes available : " + data + "/" + hex);
                    msg += hex;*/


                }

                if (msgLu) {

                    //msg = msg.toUpperCase();
                    SendMail sender = new SendMail();
                    sender.send(username, mdp, dest, "Astre", "", tab);
                    System.out.println("Success : " );
                    //180AC013000519110100B831
                    //213F001300C1 180AC01 6 000519110100 C19614BD
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }







    public static void main(String[] args) {

        ServeurEnvoie go = new ServeurEnvoie();
        go.routine();

    }

}
