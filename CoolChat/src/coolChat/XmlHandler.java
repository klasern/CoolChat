/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coolChat;

import java.awt.Color;
import java.io.StringReader;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Anders
 */
public final class XmlHandler {

    private XmlHandler() {
        throw new IllegalStateException("Do not instantiate this class.");
    }

    public static void readXml(String xmlMessage) { //ändra output till ChatTextLine
        String name;
        String message = "";
        String color;
        Boolean isBroken;
        Color textColor;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        
        boolean bText = false;
        boolean bFet = false;

        try {
            XMLEventReader eventReader
                    = factory.createXMLEventReader(new StringReader(xmlMessage));

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();

                        if (qName.equalsIgnoreCase("message")) {
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            name = attributes.next().getValue();
                            System.out.println("Namn : " + name);
                        } else if (qName.equalsIgnoreCase("text")) {
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            color = attributes.next().getValue();
                            System.out.println(color);
                            bText = true;                        
                        } 
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        
                        Characters characters = event.asCharacters();
                        if (bText) {
                            message = message + characters.getData();
                        }
                        break;
                }

            }
            System.out.println(message);
            //KONVERTERA HÄR RGB-KODEN TILL ETT COLOROBJEKT. KOLLA MESSAGE EFTER HTML MOJS
        } catch (XMLStreamException ex) {
            isBroken = true;
            System.out.println(ex);
        }

    }

    /* If message has HTML codes, it is converted to normal text */
    private String convertText(String messageIn) {
        String messageText = null;

        return messageText;
    }

    public static void main(String[] args) {
        //String message = "<name attribute=\"value\">content</name>";
        //String message = "<class><student rollno = \"393\"><firstname>dinkar</firstname><lastname>kad</lastname><nickname>dinkar</nickname><marks>85</marks></student><student rollno = \"493\"><firstname>Vaneet</firstname><lastname>Gupta</lastname><nickname>vinni</nickname><marks>95</marks></student><student rollno = \"593\"><firstname>jasvir</firstname><lastname>singn</lastname><nickname>jazz</nickname><marks>90</marks></student></class>";
        String message = "<message sender=\"Anders\"><text color=\"#RRGGBB\">Hej <fetstil>på</fetstil> dig</text></message>";
        XmlHandler.readXml(message);
    }
}
