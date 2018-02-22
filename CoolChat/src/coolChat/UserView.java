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
    private JComboBox kickComboBox;
    private JButton disconnectButton;

    /*Chat objects*/
    private List<Chat> chats;

    /* Connections */
    private List<Socket> clients;
    private List<ClientConnection> clientConnects;
    private List<ServerConnection> serverConnects;
    private ServerThread serverThread;
    public static Boolean groupChatActive = false;

    /**
     * Creates the UserView.
     */
    public UserView() {
        /*Creates Lsits*/
        chats = new ArrayList<>();
        clients = new ArrayList<>();
        clientConnects = new ArrayList<>();
        serverConnects = new ArrayList<>();

        /*Creates GUI*/
        this.setPreferredSize(new Dimension(1000, 1000));

        myTabbedPane = new JTabbedPane();
        buttonJPanel = new JPanel();
        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");

        myTabbedPane.setPreferredSize(new Dimension(800, 800));
        this.add(myTabbedPane, BorderLayout.CENTER);

        buttonJPanel.add(connectButton);
        buttonJPanel.add(disconnectButton);

        this.add(buttonJPanel, BorderLayout.NORTH);

        connectButton.addActionListener(this);

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
        myTabbedPane.add(chatIn);
        chats.add(chatIn);
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
    private void removeChat() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connectButton) { //Action when pressing connectbutton
            System.out.println("ConnectButton");
            connectMessageBox();
        }

    }

    public void connectMessageBox() {
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
                

                clientConnects.add(connection);

            } catch (NumberFormatException nfe) {
                System.out.println("Dålig Port");
            }
        }

    }

}
