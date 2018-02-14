/*
 * UserView
 * 
 * V 1.0
 *
 * 2018-01-17
 * 
 * Copyright notice
 */
package coolChat;

import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 * Main GUI and keeps track of all connections.
 */
public class UserView extends JFrame implements ActionListener {

    /*GUI*/
    private JTabbedPane myTabbedPane;
    private JButton connectButton;
    private JComboBox kickComboBox;
    private JComboBox disconnectComboBox;

    /*Chat objects*/
    private List<Chat> chats;

    /* Connections */
    private List<Socket> clients;
    private List<ClientConnection> connectedTo;
    private List<ServerConnection> connections;
    private ServerThread serverThread;
    public static Boolean groupChatActive;

    /**
     * Creates the UserView.
     */
    public UserView() {

    }

    /**
     * Adds the chat to the JTabbedPane
     *
     * @param chatIn
     */
    public void addChat(Chat chatIn) {

    }

    /**
     * Connects to another IP for chat.
     */
    private void connect() {

    }

    /**
     * Disconnects from a chat.
     */
    private void disconnectChat() {

    }
    
    /**
     * Kicks a client from server.
     */
    private void kickClient() {
        
    }
    
    /**
     * Removes a chat.
     */
    private void removeChat() {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
