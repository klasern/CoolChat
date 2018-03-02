/*
 * ChatListener
 * 
 * V 1.0
 *
 * 2018-03-01
 * 
 * Copyright notice
 */
package coolChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ChatListener class, reads input from socket.
 */
public class ChatListener extends Thread {

    private BufferedReader in;
    private PrintWriter out;
    private String message;
    private ServerConnection server;
    private ClientConnection client;
    private Socket theSocket;

    /**
     * Constructor for server.
     *
     * @param socketIn
     * @param serverIn
     * @param inReader
     * @param outWriter
     */
    public ChatListener(Socket socketIn, ServerConnection serverIn,
            BufferedReader inReader, PrintWriter outWriter) {
        theSocket = socketIn;
        server = serverIn;
        in = inReader;
        out = outWriter;
    }

    /**
     * Constructor for client.
     *
     * @param socketIn
     * @param clientIn
     */
    public ChatListener(Socket socketIn, ClientConnection clientIn) {
        theSocket = socketIn;
        client = clientIn;
        try {
            in = new BufferedReader(new InputStreamReader(
                    theSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("getInputStream failed: " + e);
        }

    }

    /**
     * Closes the connection.
     */
    public void closeConnection() {
        try {
            theSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        String chatNr;
        if (server != null) {
            chatNr = ((Integer) server.getChat().getChatNr()).toString();
        } else {
            chatNr = ((Integer) client.getChat().getChatNr()).toString();
        }

        return theSocket.getInetAddress().toString() + " in Chat " + chatNr;
    }

    public void run() {
        while (true) {
            try {
                message = in.readLine();
                if (message == null) {
                    break;     // while loop stops if no more messages
                }
                if (server != null) {
                    server.sendMessage(message, this);
                } else {
                    client.writeMessage(message);
                }
            } catch (IOException e) {
                System.out.println("readLine failed: " + e);
                break;
            }
        }
    }

    /**
     * Returns the socket.
     *
     * @return
     */
    public Socket getSocket() {
        return theSocket;
    }

    /**
     * Return ServerConnection
     *
     * @return
     */
    public ServerConnection getServer() {
        return server;
    }

    /**
     * Returns printwriters.
     * @return 
     */
    public PrintWriter getPrintWriter() {
        return out;
    }

}
