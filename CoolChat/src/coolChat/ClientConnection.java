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

import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Used to establish a connection with a server.
 */
public class ClientConnection {

    private static List<ClientConnection> clientConnects = new ArrayList<>();

    private Socket mySocket;
    private BufferedReader in;
    private PrintWriter out;

    private Chat myChat;
    private UserView myUserView;
    private ChatListener inListen;

    /**
     *
     * @param hostAddress
     * @param port
     * @param userViewIn
     */
    public ClientConnection(String hostAddress, int port, UserView userViewIn) throws IOException {
        myUserView = userViewIn;

        mySocket = new Socket(hostAddress, port);
        System.out.println("Connected to:" + hostAddress);
        out = new PrintWriter(mySocket.getOutputStream(), true);
        inListen = new ChatListener(mySocket, this);
        inListen.start();

        myChat = new Chat(this);
        myUserView.addChat(myChat);

        clientConnects.add(this);

    }

    /**
     * Return all ClientConnections.
     *
     * @return
     */
    public static List<ClientConnection> getClientConnections() {
        return clientConnects;
    }

    /**
     * Returns chat object.
     *
     * @return
     */
    public Chat getChat() {
        return myChat;
    }

    /**
     * Close connection with server.
     */
    public void closeConnection() {
        try {
            mySocket.close();
            removeClient();
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        String chatNr =  ((Integer)myChat.getChatNr()).toString();
        
        return mySocket.getInetAddress().toString()+ " in Chat " + chatNr;
    }

    /**
     * Used to send message to server which client is connected to.
     *
     * @param messageOut
     */
    public void sendMessage(String messageOut) {
        out.println(messageOut);
    }

    public void writeMessage(String message) {
        ChatTextLine messageIn = XmlHandler.readXml(message);

        if (messageIn.isDisconnectMessage()) {
            // messageIn.getName() + " has disconnected.";    /// Lägg till popup på kickad chat
            JOptionPane.showMessageDialog(myChat, "Du blev kickad av "
                    + messageIn.getName());
            myUserView.removeChat(myChat);
            closeConnection();
            removeClient();
        } else if (!messageIn.isBroken()) {
            myChat.appendToPane(messageIn.getName(), messageIn.getMessage(),
                    messageIn.getColor());
        } else {
            myChat.appendToPane("Server", "Något fel med xmlen.",
                    Color.BLACK);
        }
    }

    /**
     * Send disconect message
     */
    public void sendDisconnectMessage() {
        sendMessage(XmlHandler.disconnectMessage(myChat.getChatName()));
        removeClient();
    }

    public void sendExitMessage() {
        sendMessage(XmlHandler.disconnectMessage(myChat.getChatName()));
    }

    /**
     * Removes client form list o clientconnections.
     */
    private void removeClient() {
        clientConnects.remove(this);
    }

}
