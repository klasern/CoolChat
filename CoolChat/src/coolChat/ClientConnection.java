/*
 * ClientConnection
 * 
 * V 1.0
 *
 * 2018-03-01
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
     * Creates clientconnection.
     *
     * @param hostAddress
     * @param port
     * @param userViewIn
     */
    public ClientConnection(String hostAddress, int port, UserView userViewIn)
            throws Exception {
        myUserView = userViewIn;

        mySocket = new Socket();
        mySocket.connect(new InetSocketAddress(hostAddress, port), 5000);
        out = new PrintWriter(mySocket.getOutputStream(), true);
        inListen = new ChatListener(mySocket, this);
        inListen.start();

        myChat = new Chat(this);
        myUserView.addChat(myChat);

        clientConnects.add(this);

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

    /**
     * Removes client form list o clientconnections.
     */
    private void removeClient() {
        clientConnects.remove(this);
    }

    @Override
    public String toString() {
        String chatNr = ((Integer) myChat.getChatNr()).toString();
        return mySocket.getInetAddress().toString() + " in Chat " + chatNr;
    }

    /**
     * Used to send message to server which client is connected to.
     *
     * @param messageOut
     */
    public void sendMessage(String messageOut) {
        out.println(messageOut);
    }

    /**
     * Send disconnect message.
     */
    public void sendDisconnectMessage() {
        sendMessage(XmlHandler.disconnectMessage(myChat.getChatName()));
        removeClient();
    }

    /**
     * Send exit message.
     */
    public void sendExitMessage() {
        sendMessage(XmlHandler.disconnectMessage(myChat.getChatName()));
    }

    public void writeMessage(String message) {
        ChatTextLine messageIn = XmlHandler.readXml(message);

        if (messageIn.isDisconnectMessage() || messageIn.isRequest()) {

            if (messageIn.getName() == null) {
                JOptionPane.showMessageDialog(myChat, "Du blev nekad åtkomst.");
            } else {
                JOptionPane.showMessageDialog(myChat, "Du blev kickad av "
                        + messageIn.getName());
            }

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

}
