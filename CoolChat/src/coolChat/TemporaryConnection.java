/*
 * TemporaryConnection
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Creates a temporary connection which asks if we want client to establish a
 * connection or not.
 */
public class TemporaryConnection extends Thread {

    private BufferedReader in;
    private PrintWriter out;
    private UserView myUserView;
    private Socket clientSocket;

    /**
     * Gets pointer to UserView and creates a new thread.
     *
     * @param clientSocketIn
     * @param userViewIn
     */
    public TemporaryConnection(Socket clientSocketIn, UserView userViewIn) {
        myUserView = userViewIn;
        clientSocket = clientSocketIn;
    }

    /**
     * Adds client to groupchat.
     *
     */
    private void addToGroup() {
        List<ServerConnection> servers
                = ServerConnection.getServerConnections();

        for (ServerConnection server : servers) {
            if (server.isGroupChat()) {
                server.addChatListener(clientSocket);
            }
        }

    }

    /**
     * Create new single chat.
     *
     * @param clientSocketIn
     */
    private void createNewChat(Socket clientSocketIn) {

    }

    /**
     * Denies the connection which tries to connect.
     */
    private void denyConnection() {

    }

    /**
     * Checks if connecting client is a noob client.
     *
     * @return
     */
    private boolean isNoobClient() {
        return true;
    }

    /**
     * Used to ask if client should be added to group chat, single chat or
     * delete connection.
     */
    public void run() {
        JPanel myPanel = new JPanel();

        String[] options = {"Add to groupchat", "Create new chat",
            "Decline connection"};
        String title = "Establish Connection";
        String connectingIp = clientSocket.getInetAddress().toString();

        int selection = JOptionPane.showOptionDialog(null,
                connectingIp + " want to connect:", title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);

        System.out.println(selection + " SELECTION VALUE");

        switch (selection) {
            case -1:                                          //If closes by x
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println("x");
                }
                break;
            case 0:                                           //If add groupchat
                if (ServerConnection.groupChatActive == false) {
                    ServerConnection.groupChatActive = true;
                    ServerConnection server = new ServerConnection(clientSocket,
                            myUserView, true);
                } else {                             //Add to existing groupchat
                    addToGroup();
                }
                break;
            //server.start();
            case 1:                                             //If new chat
                ServerConnection server = new ServerConnection(clientSocket,
                        myUserView);
                break;
            case 2:                                             //if decline
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println("x");
                }
                break;
            default:
                break;
        }

    }

}
