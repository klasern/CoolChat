/*
 * ChatListener
 * 
 * V 1.0
 *
 * 2018-01-17
 * 
 * Copyright notice
 */
package coolChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author klasern
 */
public class ChatListener extends Thread{
    
    private BufferedReader in;
    private String message;
    private ServerConnection server;
    private ClientConnection client;
    private Socket theSocket;
    
    public ChatListener(Socket socketIn, ServerConnection serverIn){
        theSocket = socketIn;
        server = serverIn;
        try{
	    in = new BufferedReader(new InputStreamReader(
	            theSocket.getInputStream()));
	}catch(IOException e){
	    System.out.println("getInputStream failed: " + e);
        }
    }
    
    public ChatListener(Socket socketIn, ClientConnection clientIn) {
        theSocket = socketIn;
        client = clientIn;
        try{
	    in = new BufferedReader(new InputStreamReader(
	            theSocket.getInputStream()));
	}catch(IOException e){
	    System.out.println("getInputStream failed: " + e);
        }
        
    }
    
    public void run(){
        while(true){
	    try{
		message = in.readLine();
		if(message==null){
		    System.out.println("Client disconnect!");
		    System.exit(1);       //STÄNGER NER NÄR EN STÄNGER NER
		}
		System.out.println("Recieved: " + message);
                if (server != null) {
                    server.sendMessage(message);
                } else {
                    client.writeMessage(message);
                }
	    }catch(IOException e){
		System.out.println("readLine failed: " + e);
		System.exit(1);
	    }
	}
    }
}
