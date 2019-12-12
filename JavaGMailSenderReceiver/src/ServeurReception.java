
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ServeurReception {

    private Socket sock;
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

    }

    private void routine(){

        while(true){

            ReadEmail rd = new ReadEmail("username");
            try {
                msg = rd.read(username, mdp, addr);
            } catch( PasDeMessage e){
                System.out.println("Pas de message de "+addr);
            }

            try {
                Socket sock = new Socket(InetAddress.getLocalHost(), 15600);
                OutputStream out = sock.getOutputStream();
                out.write(msg.getBytes(),0,msg.getBytes().length);
                out.close();
                sock.close();
                Thread.sleep(3000);
            }catch (Exception e){

            }

        }


    }

    public static void main(String[] args) {

        ServeurReception go = new ServeurReception();
        go.routine();

    }

}
