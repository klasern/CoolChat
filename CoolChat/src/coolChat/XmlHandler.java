/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coolChat;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Anders
 */
public final class XmlHandler {

    private static final String[] html = {"&quot;", "&amp;", "&lt;", "&gt;",
        "&Auml;", "&Aring;", "&Ouml;", "&auml;", "&aring;", "&ouml;"};
    private static final String[] specialCharacters = {"\"", "&", "<", ">",
        "Ä", "Å", "Ö", "ä", "å", "ö"};

    private XmlHandler() {
        throw new IllegalStateException("Do not instantiate this class.");
    }

    public static ChatTextLine readXml(String xmlMessage) { 
        String name = null;
        String message = "";
        String color = null;
        Boolean isBroken = false;
        Color textColor = null;
        ChatTextLine output = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();

        boolean bText = false;

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
                            
                        } else if (qName.equalsIgnoreCase("text")) {
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            color = attributes.next().getValue();
                            
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
            /* convert RGB to color-object */
            textColor = Color.decode(color);
            output = new ChatTextLine(name, message, textColor, isBroken);
        } catch (XMLStreamException ex) {
            isBroken = true;
            ex.printStackTrace();
        }
        return output;
    }
    

    public static String writeXml(String name, String textColor, String text) {
        String messageOut = null;
        try {
            StringWriter stringWriter = new StringWriter();
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();

            XMLStreamWriter xMLStreamWriter
                    = xMLOutputFactory.createXMLStreamWriter(stringWriter);
            xMLStreamWriter.writeStartDocument();

            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement("message");
            xMLStreamWriter.writeAttribute("sender", name);

            xMLStreamWriter.writeStartElement("text");
            xMLStreamWriter.writeAttribute("color", textColor);
            xMLStreamWriter.writeCharacters(text);
            xMLStreamWriter.writeEndElement();

            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();

            xMLStreamWriter.flush();
            xMLStreamWriter.close();

            messageOut = stringWriter.getBuffer().toString();
            messageOut = messageOut.replace("<?xml version=\"1.0\" ?>"
                    + "<?xml version=\"1.0\" ?>", "");
            stringWriter.close();

        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return messageOut;
    }

    public static void main(String[] args) {
//        //String message = "<name attribute=\"value\">content</name>";
//        //String message = "<class><student rollno = \"393\"><firstname>dinkar</firstname><lastname>kad</lastname><nickname>dinkar</nickname><marks>85</marks></student><student rollno = \"493\"><firstname>Vaneet</firstname><lastname>Gupta</lastname><nickname>vinni</nickname><marks>95</marks></student><student rollno = \"593\"><firstname>jasvir</firstname><lastname>singn</lastname><nickname>jazz</nickname><marks>90</marks></student></class>";
        String message = "<message sender=\"Anders\"><text color=\"#000000\">Hej &amp; <fetstil>på</fetstil> dig</text></message>";
//        //XmlHandler.readXml(message);
          //String message = "<?xml version=\"1.0\" ?><?xml version=\"1.0\" ?><message sender=\"Anders\"><text color=\"#000000\">Hej på dig</text></message>";
//        String message = writeXml("Anders", "#RRGGBB", "Hej på dig");
//        String message1 = writeXml("Anders", "#RRGGBB", "Hej dddpå dig");
//        String message2 = writeXml("Anders", "#RRGGBB", "Hej på asdaddig");
//        System.out.println(message);
//        System.out.println(message1);
//        System.out.println(message2);
//        System.out.println(readXml(message));
          System.out.println(readXml(message).getColor());
        
    }
}

///* If message has HTML codes, it is converted to normal text */
//    private String convertText(String messageIn) {
//        String messageText = null;
//        int index = messageIn.indexOf("&");
//        while (index >= 0) {
//            System.out.println(index);
//            
//            index = messageIn.indexOf("&", index + 1);
//        }
//
//        return messageText;
//    }
