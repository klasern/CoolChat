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

    /**
     * 
     * @param hosAdress
     * @param port
     * @param userViewIn 
     */
    public ClientConnection(String hostAdress, int port, UserView userViewIn) {
        myUserView = userViewIn;
        
        /* INSÅG ATT VI BÅDE MÅSTE KUNNA SKRIVA OCH TA EMOT MEDDELANDEN NÄR SOM HELST
        , GÖR DÄRFÖR SOM I SERVERCONNECTION MED ATT SKAPA EN CHATLISTENER SOM LYSSNAR EFTER MEDDELANDEN
        */
       
        
    }
       
    /**
     * Used to send message to server which client is connected to.
     * @param messageOut 
     */
    public void sendMessage(String messageOut) {
        
    }
    
    /**
     * 
     */
    public void run(){
        
    }

}
