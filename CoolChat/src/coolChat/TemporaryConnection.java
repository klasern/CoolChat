/*
 * UserView
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
 * Creates a temporary connection which asks if we want client to establish a
 * connection or not.
 */
public class TemporaryConnection extends Thread {

    private BufferedReader in;
    private PrintWriter out;
    private UserView myUserView;

    /**
     * Gets pointer to UserView and creates a new thread.
     *
     * @param clientSocketIn
     * @param myUserView
     */
    public TemporaryConnection(Socket clientSocketIn, UserView myUserView) {

    }

    /**
     * Adds client to groupchat.
     *
     * @param clientSocketIn
     */
    private void addToGroup(Socket clientSocketIn) {

    }
    
    /**
     * Create new single chat.
     * @param clientSocketIn 
     */
    private void createNewChat(Socket clientSocketIn) {

    }
    
    /**
     * Denies the connection which tries to connect.
     */
    private void denyConnection(){
        
    }
    
    /**
     * Checks if connecting client is a noob client.
     * @return 
     */
    private boolean isNoobClient(){
        return true;
    }     
    

    /**
     * Used to ask if client should be added to group chat, single chat or
     * delete connection.
     */
    public void run() {

    }

}
