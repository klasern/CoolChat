/*
 * ServerThread
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
     * Used to get correct port.
     */
    private boolean startServerSocket() {
        try {
            port = Integer.parseInt(
                    JOptionPane.showInputDialog(myUserView,
                            "What port to listen to?"));
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Could not listen on port: " + port);
            return true;
        }
        return false;
    }

    /**
     * Ask which port to listen to and listen to that port.
     */
    public void run() {

        boolean startUp = true;

        while (startUp) {
            startUp = startServerSocket();
        }
        //Gets port to listen to.

        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                Thread temporaryThread = new TemporaryConnection(clientSocket,
                        myUserView);
                temporaryThread.start();
            } catch (IOException e) {
                System.out.println("Accept failed: " + port);
            }

        }

    }

}
