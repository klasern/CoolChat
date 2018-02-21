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

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Establish connection with a client which connects to the server of this
 * client.
 */
public class ServerConnection extends Thread {

    private List<ChatListener> inListen;
    private List<PrintWriter> outPut;
    private List<Socket> clientSockets;
    private UserView myUserView;

    public boolean isGroupChat = false;

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

        clientSockets.add(clientSocketIn);

        try {
            outPut.add(new PrintWriter(
                    clientSocketIn.getOutputStream(), true));
        } catch (IOException e) {
            System.out.println("getOutputStream failed: " + e);
            System.exit(1);  //TA BORT SENARE
        }
        
        
        /* Create a ChatListener corresponding to the first connected client */
        addChatListener(clientSocketIn);
    }

    /**
     * Sends message to all clients connected to this server.
     *
     * @param messageOut
     */
    public synchronized void sendMessage(String messageOut) {
        for (PrintWriter out : outPut) {
            out.println(messageOut);
        }
    }

    public final void addChatListener(Socket clientSocketIn) {
        inListen.add(new ChatListener(clientSocketIn, this));
    }

    public void run() {
        // Behöver denna ens vara en Thread eller runnable??
    }

    // Vill ha en metod för att lägga till i gruppchatten
}
