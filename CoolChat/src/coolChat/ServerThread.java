/*
 * ServerThread
 * 
 * V 1.0
 *
 * 2018-03-01
 * 
 * Copyright notice
 */
package coolChat;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Creates a thread which constantly listens to incomming connections on a
 * ServerSocket.
 */
public class ServerThread extends Thread {

    /*The port we listen to*/
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
     * Used to get a correct port.
     */
    private boolean startServerSocket() throws AbortException {

        /*When cancel or close window is pressed, the program closes.*/
        try {
            String input = JOptionPane.showInputDialog(myUserView,
                    "What port to listen to? (2000 to 60000)");
            /* Input is null if user cancels. Shut down program.
            User has to pick a port.
            */
            if (input == null) {
                throw new AbortException("Startup aborted by user.");
            }
            port = Integer.parseInt(input);
            if (port < 2000 || port > 60000) {
                return true;
            }
            
            serverSocket = new ServerSocket(port);

        } catch (IOException ex) {
            return true;
        } catch (NumberFormatException nfe) {
            return true;
        }

        return false;
    }

    /**
     * Ask which port to listen to and listen to that port.
     */
    public void run() {

        boolean startUp = true;

        /*Ask user for port until it is valid.*/
        while (startUp) {
            try {
                startUp = startServerSocket();
            } catch (AbortException ex) {
                System.exit(0);
            }
        }

        /*Listen to connections.*/
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
