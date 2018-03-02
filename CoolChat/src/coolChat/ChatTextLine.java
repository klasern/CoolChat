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
    private Boolean disconnectMessage;
    private Boolean request;
    private Boolean replyNo;

    /**
     * Create a ChatTextLine with default fields 
     */
    public ChatTextLine() {
        this.brokenXml = false;
        this.disconnectMessage = false;
        this.request = false;
        this.replyNo = false;

    }

    public Boolean getReplyNo() {
        return replyNo;
    }

    public void setReplyNo(Boolean replyNo) {
        this.replyNo = replyNo;
    }

    /**
     * Returns name.
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setBrokenXml(Boolean brokenXml) {
        this.brokenXml = brokenXml;
    }

    public void setDisconnectMessage(Boolean disconnetMessage) {
        this.disconnectMessage = disconnetMessage;
    }

    public void setRequest(Boolean request) {
        this.request = request;
    }

    /**
     * Returns message.
     *
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Returns color.
     *
     * @return
     */
    public Color getColor() {
        return this.textColor;
    }

    /**
     * Returns if xml is broken.
     *
     * @return
     */
    public boolean isBroken() {
        return this.brokenXml;
    }

    /**
     * Returns disconnect message;
     *
     * @return
     */
    public boolean isDisconnectMessage() {
        return this.disconnectMessage;
    }

    /**
     * Returns request message. Used to see if message contains a
     * requesttagpair.
     *
     * @return
     */
    public boolean isRequest() {
        return this.request;
    }
}
