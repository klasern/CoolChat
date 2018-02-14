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

import java.net.*;

/**
 * Creates a thread which constantly listens to incomming connections on a 
 * ServerSocket.
 */ 
public class ServerThread extends Thread{
    
    /*The port we listens to*/
    private ServerSocket serverSocket;
    private int port;
    private UserView myUserView;
    
    /**
     * Constructor used to get pointer to UserView.
     * @param userViewIn 
     */
    public ServerThread(UserView userViewIn){
        
    }
    
    /**
     * Used to start TemporaryConnection.
     */
    private void startTemporaryConnection(){
        
    }
       
    
    /**
     * Ask which port to listen to and listen to that port.
     */
    public void run(){
        
    }

    
}
