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
import java.util.*;

/**
 * Establish connection with a client which connects to the server of 
 * this client.
 */
public class ServerConnection extends Thread {
    
    private List<ChatListener> inListen;
    private List<PrintWriter> outPut;
    private Socket clientSocket;
    private UserView myUserView;
    
    public boolean isGroupChat = false;
    
    /**
     * Adds pointer to UserView and clientsocket.
     * @param clientSocketIn
     * @param userViewIn 
     */
    public ServerConnection(Socket clientSocketIn, UserView userViewIn){
        myUserView = userViewIn;
        clientSocket = clientSocketIn;        
    }
     
    /**
     * Sends message to all clients connected to this server.
     * @param messageOut 
     */
    public void sendMessage(String messageOut) {
        
    }
    
    public void run(){
        
    }
    
}
