/*
 * ChatTextLine
 * 
 * V 1.0
 *
 * 2018-03-01
 * 
 * Copyright notice
 */
package coolChat;

import java.awt.Color;

/**
 * Contains information about text to chat. 
 */
public class ChatTextLine {

    private String name;
    private String message;
    private Color textColor;
    private Boolean brokenXml;
    private Boolean disconnetMessage;
    private Boolean request;

    /**
     * Constructor for information about chat.
     * @param nameIn
     * @param messageIn
     * @param colorIn
     * @param broken
     * @param disconnect
     * @param requestIn 
     */
    public ChatTextLine(String nameIn, String messageIn, Color colorIn, 
            Boolean broken, Boolean disconnect, Boolean requestIn) {
        this.name = nameIn;
        this.message = messageIn;
        this.textColor = colorIn;
        this.brokenXml= broken;
        this.disconnetMessage = disconnect;
        this.request = requestIn;
        
    }

    /**
     * Returns name.
     * @return 
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns message.
     * @return 
     */
    public String getMessage() {
        return this.message;
    }
    
    /**
     * Returns color.
     * @return 
     */
    public Color getColor(){
        return this.textColor;
    }
    
    /**
     * Returns if xml is broken.
     * @return 
     */
    public boolean isBroken(){
        return this.brokenXml;
    }
    
    /**
     * Returns disconnect message;
     * @return 
     */
    public boolean isDisconnectMessage() {
        return this.disconnetMessage;
    }
    
    /**
     * Returns request message.
     * @return 
     */
    public boolean isRequest() {
        return this.request;
    }
}
