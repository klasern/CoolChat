/*
 * ClientConnection
 * 
 * V 1.0
 *
 * 2018-01-17
 * 
 * Copyright notice
 */
package coolChat;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to establish a connection with a server.
 */
public class ClientConnection extends Thread {

    private Socket mySocket;
    private BufferedReader in;
    private PrintWriter out;

    private Chat myChat;
    private UserView myUserView;
    private ChatListener inListen;

    /**
     * 
     * @param hostAddress
     * @param port
     * @param userViewIn 
     */
    public ClientConnection(String hostAddress, int port, UserView userViewIn) {
        myUserView = userViewIn;
        
        try {
            mySocket = new Socket(hostAddress, port);
            System.out.println("Connected to:" + hostAddress);
            out = new PrintWriter(mySocket.getOutputStream(), true);
            inListen = new ChatListener(mySocket, this);
            inListen.start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.\n" + e);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to host.\n" + e);
            System.exit(1);
        }
       
        myChat = new Chat(this);
        myUserView.addChat(myChat);        
       
        
    }
       
    /**
     * Used to send message to server which client is connected to.
     * @param messageOut 
     */
    public void sendMessage(String messageOut) {
        out.println(messageOut);
        System.out.println(messageOut); //Senare ska texten bara skrivas ut på chattfönstret
    }
    
    public void writeMessage(String message) {
        System.out.println(message);
        myChat.paintTheCanvas(message);
    }
    /**
     * 
     */
    public void run(){
        // Anslut stdIn till terminalen
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;                 
        try {
            // LÃ¤s in frÃ¥n terminalen och skicka till servern:
            while ((userInput = stdIn.readLine()) != null) {
                sendMessage(userInput);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
