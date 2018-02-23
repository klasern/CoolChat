/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coolChat;

import java.awt.Color;

/**
 *
 * @author Anders
 */
public class ChatTextLine {

    private String name;
    private String message;
    private Color textColor;

    public ChatTextLine(String nameIn, String messageIn, Color colorIn) {
        this.name = nameIn;
        this.message = messageIn;
        this.textColor = colorIn;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }
    
    public Color getColor(){
        return this.textColor;
    }

}
