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
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.\n" + e);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to host.\n" + e);
            System.exit(1);
        }
       
        
        /* INSÅG ATT VI BÅDE MÅSTE KUNNA SKRIVA OCH TA EMOT MEDDELANDEN NÄR SOM HELST
        , GÖR DÄRFÖR SOM I SERVERCONNECTION MED ATT SKAPA EN CHATLISTENER SOM LYSSNAR EFTER MEDDELANDEN
        */
       
        
    }
       
    /**
     * Used to send message to server which client is connected to.
     * @param messageOut 
     */
    public void writeMessage(String message) {
        System.out.println(message); //Senare ska texten bara skrivas ut på chattfönstret
    }
    
    /**
     * 
     */
    public void run(){
        
    }

}
