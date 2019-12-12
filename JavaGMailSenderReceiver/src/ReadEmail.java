import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ReadEmail {

    Properties props;

    public ReadEmail(String username){
        props = new Properties();
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.pop3.socketFactory.fallback", "false");
        props.put("mail.pop3.socketFactory.port", "993");
        props.put("mail.pop3.port", "993");
        props.put("mail.pop3.host", "pop.gmail.com");
        props.put("mail.pop3.user", username);
        props.put("mail.store.protocol", "imaps");
    }

    public String read(String username,String mdp,String addrSender) throws PasDeMessage{

        // 2. Creates a javax.mail.Authenticator object.
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, mdp);
            }
        };

        // 3. Creating mail session.
        Session session = Session.getDefaultInstance(props, auth);

        try {
            // 4. Get the POP3 store provider and connect to the store.
            Store store = session.getStore("imaps");
            store.connect("pop.gmail.com", username, mdp);

            // Affiche les folder dispo sur la boite
            Folder[] f = store.getFolder("[Gmail]").list();

            //for(Folder fd:f)
            //    System.out.println(">> "+fd.getName());

            // 5. Get folder and open the INBOX folder in the store.

            //Folder Tous les messages (le nom peu changer en fonction de la langue)
            String folderName = f[f.length-1].getName();
            System.out.println("Nom du folder de recherche >> " + folderName);
            Folder inbox = store.getFolder("[Gmail]/"+folderName);
            inbox.open(Folder.READ_ONLY);

            // 6. Retrieve the messages from the folder.
            Message[] messages = inbox.getMessages();

            int i = messages.length-1;
            boolean msgVu = false;
            while (i >= 0 && !msgVu){
                //recuperation de l'expediteur
                Address[] froms = messages[i].getFrom();
                String emailaddr = froms == null ? null : ((InternetAddress) froms[0]).getAddress();

                if (emailaddr.equals(addrSender)){
                    //Message à lire
                    Message message = messages[i];
                    msgVu = true;

                    // 7. Close folder and close store.
                    inbox.close(false);
                    store.close();
                    return getTextFromMessage(message);
                }
                i--;
            }

            // 7. Close folder and close store.
            inbox.close(false);
            store.close();

        } catch(MessagingException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }


        System.out.println("Expediteur non trouvé");
        throw(new PasDeMessage());
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            //Si la partie est un fichier
            if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                System.out.println("Il y a une piece jointe : ");
                System.out.println(bodyPart.getFileName());

                InputStream stream =
                        (InputStream) bodyPart.getInputStream();

                int r = stream.read();

                return(Integer.toString(r));
                /*
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(stream));
                while (bufferedReader.ready()) {
                    System.out.println(bufferedReader.readLine());
                }
                */

            }
            else if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }
}

