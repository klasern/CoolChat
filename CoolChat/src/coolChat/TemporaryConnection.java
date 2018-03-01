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
                server.addChatListener(clientSocket, in, out);
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
        String message = "";
        ChatTextLine checkMessage;
        String acceptString;
        boolean advancedClient;

        JPanel myPanel = new JPanel();

        String[] options = {"Add to groupchat", "Create new chat",
            "Decline connection"};
        String title = "Establish Connection";
        String connectingIp = clientSocket.getInetAddress().toString();

        try {
            in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            out = new PrintWriter(
                            clientSocket.getOutputStream(), true);
            message = in.readLine();            
        } catch (IOException ex) {
            Logger.getLogger(TemporaryConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        checkMessage = XmlHandler.readXml(message);
        advancedClient = checkMessage.isRequest();

        if (advancedClient) {
            acceptString = checkMessage.getMessage();
        } else {
            acceptString = " is not advanced client.";
        }

        int selection = JOptionPane.showOptionDialog(myUserView,
                connectingIp + " wants to connect: " + acceptString, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);

        System.out.println(selection + " SELECTION VALUE");

        switch (selection) {
            case -1:                                          //If closes by x
                try {                    
                    if (advancedClient) {
                        out.println(XmlHandler.requestNoMessage());
                    } else {
                        out.println(XmlHandler.disconnectMessage("Server"));
                    }
                    out.close();
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println("x");
                    ex.printStackTrace();
                }
                break;
            case 0:                                           //If add groupchat
                if (ServerConnection.groupChatActive == false) {
                    ServerConnection.groupChatActive = true;
                    ServerConnection server = new ServerConnection(clientSocket,
                            myUserView, true, in, out);
                } else {                             //Add to existing groupchat
                    addToGroup();
                }
                if (!advancedClient) {
                    List<ServerConnection> servers
                            = ServerConnection.getServerConnections();
                    for (ServerConnection server : servers) {
                        if (server.isGroupChat()) {
                            server.sendMessage(message);
                        }
                    }
                }
                break;
            //server.start();
            case 1:                                             //If new chat
                ServerConnection server = new ServerConnection(clientSocket,
                        myUserView, in, out);
                if (!advancedClient) {
                    server.sendMessage(message);
                }
                break;
            case 2:                                             //if decline
                try {
                    out = new PrintWriter(
                            clientSocket.getOutputStream(), true);
                    if (advancedClient) {
                        out.println(XmlHandler.requestNoMessage());
                    } else {
                        out.println(XmlHandler.disconnectMessage("Server"));
                    }
                    out.close();
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println("x");
                    ex.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

}
