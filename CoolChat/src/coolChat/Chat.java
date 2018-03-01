/*
 * Chat
 *
 * V 1.0
 * 
 * 2018-03-01
 */
package coolChat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * A graphic component used for a single chat-window in UserView. Also exchanges
 * data with the connection-object corresponding to the chat.
 */
public class Chat extends JPanel implements ActionListener {

    /* Number of chats created */
    public static int createdChats = 1;

    /* This chats' number */
    private int chatNr;

    /*Graphic components needed for a single chat-window */
    private JScrollPane myScrollPane;
    private JTextField myTextField;
    private JTextPane chatTextPane;
    private JButton sendButton;
    private JButton colorButton;
    private JButton nameButton;
    private JPanel bottomPanel;

    /* Used to add lines of text to the chat */
    private DefaultStyledDocument document;
    private Style style;
    private StyleContext context;

    /* The username and chatcolor for this specific chat. */
    private String name;
    private Color textColor;
    private String color;                               //Color as RGB string

    /* The chat exchanges data with EITHER a ServerConnectio or a 
    ClientConnection */
    private ServerConnection server;
    private ClientConnection client;

    /**
     * Creates a chat object used to write text.
     */
    public Chat() {
        chatNr = createdChats;
        createdChats ++;

        /* Used to append text to the chatTextPane*/
        document = new DefaultStyledDocument();
        context = new StyleContext();
        style = context.addStyle("test", null);

        /* Initializing graphic components */
        chatTextPane = new JTextPane(document);
        myTextField = new JTextField();
        sendButton = new JButton("Send");
        colorButton = new JButton("Change color");
        nameButton = new JButton("Change name");

        /* Panel to contain textfield and buttons */
        bottomPanel = new JPanel();

        /* Set default name and color */
        name = "Petter-Niklas";
        textColor = Color.magenta;
        color = "#" + Integer.toHexString(textColor.getRGB()).substring(2);

        /* Add to bottompanel */
        myTextField.setPreferredSize(new Dimension(600, 30));
        bottomPanel.add(myTextField);
        bottomPanel.add(sendButton);
        bottomPanel.add(colorButton);
        bottomPanel.add(nameButton);

        /* Create scrollpane and add the chattextpane to it */
        myScrollPane = new JScrollPane(chatTextPane);

        /* Set the size of the textpane and make non editable*/
        chatTextPane.setPreferredSize(new Dimension(800, 800));
        chatTextPane.setEditable(false);

        /* Add actionlisteners */
        colorButton.addActionListener(this);
        nameButton.addActionListener(this);
        sendButton.addActionListener(this);
        myTextField.addActionListener(this);

        /* Add components to the the chat object */
        setLayout(new BorderLayout());
        add(myScrollPane, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

    }

    /**
     * Constructor used for chat when user is server.
     *
     * @param serverIn
     */
    public Chat(ServerConnection serverIn) {
        this();
        this.server = serverIn;
    }

    /**
     * Constructor used for chat when user is client.
     *
     * @param clientIn
     */
    public Chat(ClientConnection clientIn) {
        this();
        this.client = clientIn;
    }

    /**
     * Adds a line of text to the chatTextPane with given name and color.
     *
     * @param nameIn
     * @param msg
     * @param c
     */
    public void appendToPane(String nameIn, String msg, Color c) {
        StyleConstants.setForeground(style, c);

        try {
            document.insertString(document.getLength(), nameIn + ": "
                    + msg + "\n", style);
            /*Keeps TextPane at the bottom (i.e. autoscrolls down). */
            chatTextPane.setCaretPosition(
                    chatTextPane.getDocument().getLength());
        } catch (BadLocationException e) {
        }

    }

    /**
     * Actions when pressing send-, color-, and namebuttons. Also action 
     * when written in textfield.
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == colorButton) {
            Color newColor = JColorChooser.showDialog(this, 
                    "Change Button Background", textColor);
            textColor = newColor;
            color = "#" + Integer.toHexString(textColor.getRGB()).substring(2);            
        } else if (e.getSource() == sendButton) {
            SendMessage(myTextField.getText());
        } else if (e.getSource() == nameButton) {
            String newName = JOptionPane.showInputDialog(this,
                    "Choose a new name! You can be anyone!");
            if (newName != null) {
                name = newName;
            }
        } else if (e.getSource() == myTextField) {
            SendMessage(myTextField.getText());
        }
    }

    /* Sends message when ENTER or Sendbutton i pressed */
    private void SendMessage(String message) {
        String messageOut = XmlHandler.writeXml(name, color, message);
        if (server != null) {
            server.sendMessage(messageOut);
            myTextField.setText("");
        } else {                               //server = null => client != null
            client.sendMessage(messageOut);
            myTextField.setText("");
        }
    }

    /**
     * Returns name of chat.
     *
     * @return
     */
    public String getChatName() {
        return name;
    }

    /**
     * Returns chat number.
     *
     * @return
     */
    public int getChatNr() {
        return chatNr;
    }
}
