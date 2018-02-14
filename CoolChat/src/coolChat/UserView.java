/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coolChat;

import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
/**
 *
 * @author klasern
 */
public class UserView extends JFrame implements ActionListener {
    
    private JTabbedPane myTabbedPane;
    private List<Chat> chats;
    private List<Socket> clients;
    private List<ClientConnection> connectedTo;
    private List<ServerConnection> connections;
    private JButton connectButton;
    private ServerThread serverThread;
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
