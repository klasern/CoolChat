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
import javax.swing.*;

/**
 * Creates a thread which constantly listens to incomming connections on a
 * ServerSocket.
 */
public class ServerThread extends Thread {

    /*The port we listens to*/
    private ServerSocket serverSocket;
    private int port;
    private UserView myUserView;

    /**
     * Constructor used to get pointer to UserView, and start the thread which
     * listens to the serversocket.
     *
     * @param userViewIn
     */
    public ServerThread(UserView userViewIn) {
        myUserView = userViewIn;
    }

    /**
     * Used to start TemporaryConnection.
     */
    private void startTemporaryConnection() {

    }

    /**
     * Ask which port to listen to and listen to that port.
     */
    public void run() {

        //Gets port to listen to.
        port = Integer.parseInt(
                JOptionPane.showInputDialog(myUserView, 
                        "What port to listen to?"));

        //Starts server socket
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + port);
            System.exit(-1);
        }

        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Accept failed: " + port);
                System.exit(-1);
            }
            Thread temporaryThread = new TemporaryConnection(clientSocket, 
                    myUserView);            
            temporaryThread.start();            
            
            
        }

    }

}
