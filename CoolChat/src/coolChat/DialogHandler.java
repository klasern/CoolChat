/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coolChat;

import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Anders
 */
public class DialogHandler implements ActionListener {
    
    UserView myUserView;
    
    public DialogHandler(UserView inView){
        this.myUserView = inView;
    }
    
    public void connectMessageBox(UserView inView) {
        JDialog myDialog = new JDialog(inView, "Connect");
        JPanel myPanel = new JPanel();
        String serverIp;   //If connect will use this field
        int portNr;     //If connect will use this field

        /*Creates Jpanel to add to JOptionPane for input ip and port.*/
        myPanel.setLayout(new GridLayout(0, 2, 2, 2));

        JTextField serverIpIn = new JTextField(10);
        JTextField portNrIn = new JTextField(10);
        
        JButton connectButton = new JButton("Connect");
        JButton cancelButton = new JButton("Cancel");

        myPanel.add(new JLabel("Server IP: "));
        myPanel.add(serverIpIn);
        myPanel.add(new JLabel("Port number:"));
        myPanel.add(portNrIn);
        
        myPanel.add(connectButton);
        myPanel.add(cancelButton);
        
        myDialog.getContentPane().add(myPanel);
        myDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        connectButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        myDialog.pack();
        myDialog.setVisible(true);

    }
    
    public static void main(String[] args) {

        
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
