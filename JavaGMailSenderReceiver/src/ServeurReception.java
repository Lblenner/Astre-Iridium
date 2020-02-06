
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ServeurReception {

    private Socket sock;
    private OutputStream out;
    private String username;
    private String mdp;
    private String addr;
    private String msg;
    Scanner scan = new Scanner(System.in);

    public ServeurReception() {

        //Parametre pour l'envoie de Mail
        System.out.print("Entrez votre adresse gmail: ");
        username = scan.next();
        System.out.print("Entrez votre mot de passe application : ");
        mdp = scan.next();
        System.out.print("Entrez l'adresse de laquelle nous voulons voir les mails : ");
        addr = scan.next();

        try {
            sock = new Socket(InetAddress.getLocalHost(),15600);
            out = sock.getOutputStream();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void routine(){

        //while(true){

            ReadEmail rd = new ReadEmail("username");
            try {
                msg = rd.read(username, mdp, addr);
                out.write(msg.getBytes(),0,msg.getBytes().length);
            } catch( Exception e ){ }


        //}


    }

    public static void main(String[] args) {

        ServeurReception go = new ServeurReception();
        go.routine();

    }

}
