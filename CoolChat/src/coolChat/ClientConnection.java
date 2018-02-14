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
    public ClientConnection(String hosAdress, int port, UserView userViewIn) {
        myUserView = userViewIn;
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
