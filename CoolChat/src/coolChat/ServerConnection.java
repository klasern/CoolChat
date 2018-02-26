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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Establish connection with a client which connects to the server of this
 * client.
 */
public class ServerConnection extends Thread {

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

        clientSockets.add(clientSocketIn);
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
     * @return 
     */
    public List<ChatListener> getChatListeners(){
        return inListen;
    }

    /**
     * Return chat.
     * @return 
     */
    public Chat getChat(){
        return myChat;
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
        System.out.println(messageOut);
        myChat.paintTheCanvas(messageOut);
    }

    public final void addChatListener(Socket clientSocketIn) {
        try {
            outPut.add(new PrintWriter(
                    clientSocketIn.getOutputStream(), true));
        } catch (IOException e) {
            System.out.println("getOutputStream failed: " + e);
            System.exit(1);  //TA BORT SENARE
        }

        ChatListener listener = new ChatListener(clientSocketIn, this);
        inListen.add(listener);
        listener.start();
    }

    public void run() {
        // Behöver denna ens vara en Thread eller runnable??

        /* Test om denna måste vara tråd eller ej */
        // Anslut stdIn till terminalen
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        try {
            // LÃ¤s in frÃ¥n terminalen och skicka till servern:
            while ((userInput = stdIn.readLine()) != null) {
                sendMessage(userInput);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Vill ha en metod för att lägga till i gruppchatten
}
