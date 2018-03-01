/*
 * UserView
 * 
 * V 1.0
 *
 * 2018-03-01
 * 
 * Copyright notice
 */
package coolChat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
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

    /* Connections */
    private ServerThread serverThread;

    /**
     * Creates the UserView.
     */
    public UserView() {
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

        /*Send disconnect messages on close. */
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                /*Disconnects all client conections*/
                List<ClientConnection> clientConnects
                        = ClientConnection.getClientConnections();
                for (ClientConnection closeClient : clientConnects) {
                    closeClient.sendExitMessage();
                }

                /*Disconnects from all servers*/
                List<ServerConnection> servers
                        = ServerConnection.getServerConnections();
                for (ServerConnection closeServer : servers) {
                    List<ChatListener> serverClients
                            = closeServer.getChatListeners();
                    for (ChatListener closeClients : serverClients) {
                        closeServer.sendExitMessage(closeClients);
                    }
                }
            }
        });
    }

    /**
     * Adds the chat to the JTabbedPane.
     *
     * @param chatIn
     */
    public void addChat(Chat chatIn) {
        myTabbedPane.add("Chat " + chatIn.getChatNr(), chatIn);
    }

    /**
     * Removes a chat from GUI.
     */
    public void removeChat(Chat chatIn) {
        myTabbedPane.remove(chatIn);
    }

    /**
     * Actions when pressing connect-,disconnect- and kickbutton.
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {        
        if (e.getSource() == connectButton) {
            
            /*Creates new thread to avoid program freezing when connecting to an 
            IP where noone is listening. */
            Thread connectThread = new Thread() {
                public void run() {
                    connectMessageBox();
                }
            };
            connectThread.start();            
        } else if (e.getSource() == disconnectButton) {
            disconnectMessageBox();
        } else if (e.getSource() == kickButton) {
            kickMessageBox();
        }

    }

    /**
     * Opens a messageox where user can input IP, port number, and connecting 
     * message which it wants to connect to.
     */
    private void connectMessageBox() {
        JPanel myPanel = new JPanel();
        String serverIp;   
        int portNr;     

        /*Builds the message box.*/
        myPanel.setLayout(new GridLayout(0, 2, 2, 2));
        JTextField serverIpIn = new JTextField(0);
        JTextField portNrIn = new JTextField(10);
        JTextField requestMsg = new JTextField(10);
        requestMsg.setText("JAG VILL ANSLUTA");
        myPanel.add(new JLabel("Server IP: "));
        myPanel.add(serverIpIn);
        myPanel.add(new JLabel("Port number:"));
        myPanel.add(portNrIn);
        myPanel.add(new JLabel("Meddelande:"));
        myPanel.add(requestMsg);

        /*View the popup and take input choice.*/
        int option = JOptionPane.showConfirmDialog(this, myPanel, "Connect",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.YES_OPTION) { 

            try {
                serverIp = serverIpIn.getText();
                portNr = Integer.parseInt(portNrIn.getText());

                ClientConnection connection = new ClientConnection(serverIp,
                        portNr, this);

                /*Send request message to server.*/
                connection.sendMessage(XmlHandler.requestMessage(
                        requestMsg.getText()));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "DÅLIG PORT/IP");
            }
        }

    }

    /**
     * Creates a disconnectMessageBox where user can choose which IP to
     * disconnect from.
     */
    private void disconnectMessageBox() {
        JPanel myPanel = new JPanel();
        
        /*List of all servers connected to.*/
        List<ClientConnection> clientConnects
                = ClientConnection.getClientConnections();
        
        JComboBox<ClientConnection> disconnectBox = new JComboBox<>();

        for (ClientConnection addClient : clientConnects) {
            disconnectBox.addItem(addClient);
        }

        String[] options = {"OK", "Cancel"};
        String title = "Disconnect User";

        int selection = JOptionPane.showOptionDialog(this, disconnectBox, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);

        /*If choice is OK, removes server and chat from GUI.*/
        if (selection == 0) {
            Object selectedDisconnect = disconnectBox.getSelectedItem();
            removeChat(((ClientConnection) selectedDisconnect).getChat());

            ((ClientConnection) selectedDisconnect).sendDisconnectMessage();
            ((ClientConnection) selectedDisconnect).closeConnection();
        }

    }

    /**
     * A messagebox where user can decide which client to kick.
     */
    private void kickMessageBox() {
        JPanel myPanel = new JPanel();

        List<ServerConnection> servers
                = ServerConnection.getServerConnections();

        JComboBox<ChatListener> kickBox = new JComboBox<>();

        /*All clients connected to our servers added to JComboBox.*/
        for (ServerConnection addServer : servers) {
            List<ChatListener> serverClients = addServer.getChatListeners();
            for (ChatListener addClients : serverClients) {
                kickBox.addItem(addClients);
            }
        }

        String[] options = {"OK", "Cancel"};
        String title = "Kick User";

        int selection = JOptionPane.showOptionDialog(this, kickBox, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);

        /*If OK removes client from server and kicks it out.*/
        if (selection == 0) { 
            Object selectedKick = kickBox.getSelectedItem();

            ServerConnection kickServer
                    = ((ChatListener) selectedKick).getServer();

            /*Removes chat if server is not groupchat.*/
            if (!kickServer.isGroupChat()) {
                removeChat(kickServer.getChat());
            }

            kickServer.sendDisconnectMessage((ChatListener) selectedKick);
        }
    }
}
