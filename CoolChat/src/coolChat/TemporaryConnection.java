/*
 * TemporaryConnection
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
     * Used to ask if client should be added to group chat, single chat or
     * deny connection.
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
            ex.printStackTrace();
        }

        /*Reads first message and checks if it contains request tags.*/
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

        switch (selection) {
            case 0:                                           //If add groupchat
                if (ServerConnection.groupChatActive == false) {
                    ServerConnection.groupChatActive = true;
                    ServerConnection server = new ServerConnection(clientSocket,
                            myUserView, true, in, out);
                } else {                             
                    addToGroup();
                }
                /*If not advanced client added to groupchat distributes 
                the connecting message.*/
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
            case 1:                                             //If new chat
                ServerConnection server = new ServerConnection(clientSocket,
                        myUserView, in, out);
                if (!advancedClient) {
                    server.sendMessage(message);
                }
                break;
            case -1:                                            //if closed by x
            case 2:                                             //if decline
                try {
                    out = new PrintWriter(
                            clientSocket.getOutputStream(), true);
                    /*If advanced client return decline tags, 
                    otherwise return disconnect tag.*/
                    if (advancedClient) {                                               
                        out.println(XmlHandler.requestNoMessage());
                    } else {
                        out.println(XmlHandler.disconnectMessage("Server"));
                    }
                    out.close();
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
