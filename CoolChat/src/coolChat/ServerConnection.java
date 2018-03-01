/*
 * ServerConnection
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
import javax.swing.JOptionPane;

/**
 * Establish connection with a client which connects to the server of this
 * client.
 */
public class ServerConnection {

    private List<ChatListener> inListen;
    private List<PrintWriter> outPut;
    private List<Socket> clientSockets;
    private UserView myUserView;
    private Chat myChat;

    private static List<ServerConnection> serverConnects = new ArrayList<>();

    public static Boolean groupChatActive = false;
    private boolean isGroupChat = false;

    /**
     * Adds pointer to UserView and clientsocket.
     *
     * @param clientSocketIn
     * @param userViewIn
     */
    public ServerConnection(Socket clientSocketIn, UserView userViewIn) {
        inListen = new ArrayList<ChatListener>();
        outPut = new ArrayList<PrintWriter>();
        clientSockets = new ArrayList<Socket>();
        myUserView = userViewIn;

        serverConnects.add(this);

        myChat = new Chat(this);
        myUserView.addChat(myChat);

        /* Create a ChatListener corresponding to the first connected client */
        addChatListener(clientSocketIn);

    }

    /**
     * Constructor for groupchat.
     *
     * @param clientSocketIn
     * @param userViewIn
     * @param groupChatIn
     */
    public ServerConnection(Socket clientSocketIn, UserView userViewIn,
            boolean groupChatIn) {
        this(clientSocketIn, userViewIn);
        isGroupChat = groupChatIn;
    }

    /**
     * Returns if serversocket is groupchat or not.
     *
     * @return
     */
    public boolean isGroupChat() {
        return isGroupChat;
    }

    /**
     * Return all created serverConnections.
     *
     * @return
     */
    public static List<ServerConnection> getServerConnections() {
        return serverConnects;
    }

    /**
     * Return all ChatListener which listens to this server.
     *
     * @return
     */
    public List<ChatListener> getChatListeners() {
        return inListen;
    }

    /**
     * Return chat.
     *
     * @return
     */
    public Chat getChat() {
        return myChat;
    }

    /**
     * Removes client and its printwriter.
     *
     * @param listenIn
     */
    public void removeClient(ChatListener listenIn) {
        int removeIndex = inListen.indexOf(listenIn);

        if (!isGroupChat) {
            serverConnects.remove(this);
        }

        try {
            clientSockets.get(removeIndex).close();
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        clientSockets.remove(removeIndex);
        outPut.remove(removeIndex);
        inListen.remove(removeIndex);
    }

    /**
     * Send disconnect message to kicked client and removes it from the lists.
     *
     * @param listenIn
     */
    public void sendDisconnectMessage(ChatListener listenIn) {
        int dcIndex = inListen.indexOf(listenIn);
        outPut.get(dcIndex).println(
                XmlHandler.disconnectMessage(myChat.getChatName()));
        removeClient(listenIn);
    }

    public void sendExitMessage(ChatListener listenIn) {
        int dcIndex = inListen.indexOf(listenIn);
        outPut.get(dcIndex).println(
                XmlHandler.disconnectMessage(myChat.getChatName()));

    }

    /**
     * Sends message to all clients connected to this server. theSocket
     *
     * @param messageOut
     * @param chatIn
     */
    public synchronized void sendMessage(String messageOut, ChatListener chatIn) {
        /* Create a chattextline-object that contains information to write on 
        our own screen
         */
        ChatTextLine message = XmlHandler.readXml(messageOut);

        if (message.isDisconnectMessage()) {
            messageOut = message.getName() + " has disconnected.";
            if (!isGroupChat) {
                JOptionPane.showMessageDialog(myChat, messageOut);
                myUserView.removeChat(myChat);
            } else {                
                myChat.appendToPane("Server: ", messageOut,
                        Color.BLACK);
                String discOut = XmlHandler.writeXml("Server: ", "#000000", 
                        messageOut);
                for (PrintWriter out : outPut) {
                    out.println(discOut);
                }

            }
            removeClient(chatIn);
        } else {
            if (message.isBroken()) {
                myChat.appendToPane("Server", "Något fel med xmlen.",
                        Color.BLACK);
            } else {
                myChat.appendToPane(message.getName(), message.getMessage(),
                        message.getColor());
            }

            /* Sends the xml-string to all clients */
            for (PrintWriter out : outPut) {
                out.println(messageOut);
            }
        }
    }

    /**
     * Used when something is written from chat.
     *
     * @param messageOut
     */
    public synchronized void sendMessage(String messageOut) {
        ChatTextLine message = XmlHandler.readXml(messageOut);

        if (message.isBroken()) {
            myChat.appendToPane("Server", "Något fel med xmlen.",
                    Color.BLACK);
        } else {
            myChat.appendToPane(message.getName(), message.getMessage(),
                    message.getColor());
        }

        for (PrintWriter out : outPut) {
            out.println(messageOut);
        }
    }

    public final void addChatListener(Socket clientSocketIn) {
        try {
            clientSockets.add(clientSocketIn);
            outPut.add(new PrintWriter(
                    clientSocketIn.getOutputStream(), true));
            ChatListener listener = new ChatListener(clientSocketIn, this);
            inListen.add(listener);
            listener.start();
        } catch (IOException e) {
            System.out.println("getOutputStream failed: " + e);
            //System.exit(1);  //TA BORT SENARE
        }

    }

    // Vill ha en metod för att lägga till i gruppchatten
}
