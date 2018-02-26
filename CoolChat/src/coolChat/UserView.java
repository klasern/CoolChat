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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
    private JPanel buttonJPanel;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton kickButton;

    /*Chat objects*/
    private List<Chat> chats;

    /* Connections */
    private List<Socket> clients;
    //private List<ClientConnection> clientConnects;
    private ServerThread serverThread;

    private int chatNr = 1;

    /**
     * Creates the UserView.
     */
    public UserView() {
        /*Creates Lsits*/
        chats = new ArrayList<>();
        clients = new ArrayList<>();
        //clientConnects = new ArrayList<>();
        //serverConnects = new ArrayList<>();

        /*Creates GUI*/
        this.setPreferredSize(new Dimension(1000, 1000));

        myTabbedPane = new JTabbedPane();
        buttonJPanel = new JPanel();
        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        kickButton = new JButton("Kick");

        myTabbedPane.setPreferredSize(new Dimension(800, 800));
        this.add(myTabbedPane, BorderLayout.CENTER);

        buttonJPanel.add(connectButton);
        buttonJPanel.add(disconnectButton);
        buttonJPanel.add(kickButton);

        this.add(buttonJPanel, BorderLayout.NORTH);

        connectButton.addActionListener(this);
        disconnectButton.addActionListener(this);
        kickButton.addActionListener(this);

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        serverThread = new ServerThread(this);
        serverThread.start();

    }

    /**
     * Adds the chat to the JTabbedPane
     *
     * @param chatIn
     */
    public void addChat(Chat chatIn) {
        myTabbedPane.add("Chat " + chatNr, chatIn);
        chats.add(chatIn);
        chatNr += 1;
    }

    /**
     * Connects to another IP for chat.
     */
    private void connect(String serverIpIn, int portNrIn) {

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
    private void removeChat(Chat chatIn) {
        myTabbedPane.remove(chatIn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connectButton) { //Action when pressing connectbutton
            System.out.println("ConnectButton");
            connectMessageBox();
        } else if (e.getSource() == disconnectButton) {
            System.out.println("disconnectButton");
            disconnectMessageBox();
        } else if (e.getSource() == kickButton) {
            System.out.println("kickButton");
            kickMessageBox();
        }

    }

    /**
     * Opens a messageox where user can input IP, and port number which it wants
     * to connect to.
     */
    private void connectMessageBox() {
        JPanel myPanel = new JPanel();
        String serverIp;   //If connect will use this field
        int portNr;     //If connect will use this field

        /*Creates Jpanel to add to JOptionPane for input ip and port.*/
        myPanel.setLayout(new GridLayout(0, 2, 2, 2));

        JTextField serverIpIn = new JTextField(10);
        JTextField portNrIn = new JTextField(10);

        myPanel.add(new JLabel("Server IP: "));
        myPanel.add(serverIpIn);
        myPanel.add(new JLabel("Port number:"));
        myPanel.add(portNrIn);

        int option = JOptionPane.showConfirmDialog(this, myPanel, "Connect",
                JOptionPane.OK_CANCEL_OPTION);

        System.out.println(JOptionPane.YES_OPTION + " yes value");
        System.out.println(JOptionPane.CANCEL_OPTION + " cancel value");
        System.out.println(serverIpIn.getText() + " - " + portNrIn.getText());

        if (option == JOptionPane.YES_OPTION) { //If yes creates a clientConnection
            try {
                serverIp = serverIpIn.getText();
                portNr = Integer.parseInt(portNrIn.getText());

                ClientConnection connection = new ClientConnection(serverIp,
                        portNr, this);

                //clientConnects.add(connection);
                //connection.start();
            } catch (NumberFormatException nfe) {
                System.out.println("Dålig Port");
            }
        }

    }

    /**
     * Creates a disconnectMessageBox where user can choose which IP to
     * disconnect from.
     */
    private void disconnectMessageBox() {
        JPanel myPanel = new JPanel();

        List<ClientConnection> clientConnects
                = ClientConnection.getClientConnections();

        JComboBox<ClientConnection> disconnectBox = new JComboBox<>();

        for (ClientConnection addClient : clientConnects) {
            disconnectBox.addItem(addClient);
        }

        String[] options = {"OK", "Cancel"};
        String title = "Disconnect User";

        int selection = JOptionPane.showOptionDialog(null, disconnectBox, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);

        if (selection == 0) { // Om OK ta ut det valda objektet ur comboboxen och se till att disconnecta från clienten med den IPn
            Object selectedDisconnect = disconnectBox.getSelectedItem();
            removeChat(((ClientConnection) selectedDisconnect).getChat());
            ((ClientConnection) selectedDisconnect).closeConnection();
        }

    }

    /**
     * A messagebox where user can decide which client to kick.
     */
    private void kickMessageBox() { //Eventuell kodupprepning som kan fixas med disconnectbox
        JPanel myPanel = new JPanel();

        //String[] test = {"Anders", "Klas", "Ok", "Test"}; //Lägg in IPs i ARRAYN  
        List<ServerConnection> servers = 
                ServerConnection.getServerConnections();

        JComboBox<ChatListener> kickBox = new JComboBox<>();

        for (ServerConnection addServer : servers) {
            List<ChatListener> serverClients = addServer.getChatListeners();
            for (ChatListener addClients : serverClients) {
                kickBox.addItem(addClients);
            }
        }

        String[] options = {"OK", "Cancel"};
        String title = "Kick User";

        int selection = JOptionPane.showOptionDialog(null, kickBox, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);

        if (selection == 0) { // Om OK ta ut det valda objektet ur comboboxen och se till att disconnecta från clienten med den IPn
            Object selectedKick = kickBox.getSelectedItem();

            ServerConnection kickServer
                    = ((ChatListener) selectedKick).getServer();

            if (!kickServer.isGroupChat()) { //Removes chat if server is not groupchat
                removeChat(kickServer.getChat());
            }

            ((ChatListener) selectedKick).closeConnection();

        }
    }

}
