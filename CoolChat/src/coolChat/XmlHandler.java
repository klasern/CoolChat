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
import java.util.jar.Attributes;
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

    private XmlHandler() {
        throw new IllegalStateException("Do not instantiate this class.");
    }

    public static ChatTextLine readXml(String xmlMessage) {
        String name = null;
        String message = "";
        String color = null;
        Boolean isBroken = true;
        Boolean disconnect = false;
        Color textColor = null;
        ChatTextLine output = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();

        boolean bText = false;
        boolean bRequest = false;

        Attribute nextName;
        Attribute nextColor;

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
                            isBroken = false;
                            Iterator<Attribute> attributes
                                    = startElement.getAttributes();
                            while (name == null && attributes.hasNext()) {
                                nextName = attributes.next();
                                if (nextName.getName().toString()
                                        .equals("sender")) {
                                    name = nextName.getValue();
                                }
                            }

                        } else if (qName.equalsIgnoreCase("text")) {
                            Iterator<Attribute> attributes
                                    = startElement.getAttributes();
                            while (color == null && attributes.hasNext()) {
                                nextColor = attributes.next();
                                if (nextColor.getName().toString() //Stilguiden??
                                        .equals("color")) {
                                    color = nextColor.getValue();
                                }
                            }

                            bText = true;
                        } else if (qName.equalsIgnoreCase("disconnect")) {
                            message = "";
                            textColor = Color.BLACK;
                            isBroken = false;
                            disconnect = true;
                            output = new ChatTextLine(name, message, textColor, 
                                    isBroken, disconnect, bRequest);
                            return output;
                        } else if (qName.equalsIgnoreCase("request")) {
                            bRequest = true;
                            bText = false; //Om någon skickat med messagetaggar struntar vi i det
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:

                        Characters characters = event.asCharacters();
                        if (bText) { //Om requesttaggar saknas (dvs enklare användare, så får vi ett vanligt message istället
                            message = message + characters.getData();
                        } else if (bRequest) {
                            message = characters.getData();
                                   
                        }
                        break;
                }

            }
            /* convert RGB to color-object */
            textColor = Color.decode(color);
        } catch (XMLStreamException ex) {
            name = "";
            message = "";
            textColor = Color.BLACK;
            isBroken = true;
            ex.printStackTrace();
        } finally {
            output = new ChatTextLine(name, message, textColor, isBroken, 
                    disconnect, bRequest);
            return output; // If isBroken, still need to return 
        }

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

    public static String disconnectMessage(String nameIn) {
        return "<message sender=\"" + nameIn + "\"><disconnect /></message>";
    }

    public static void main(String[] args) {
//        //String message = "<name attribute=\"value\">content</name>";
//        //String message = "<class><student rollno = \"393\"><firstname>dinkar</firstname><lastname>kad</lastname><nickname>dinkar</nickname><marks>85</marks></student><student rollno = \"493\"><firstname>Vaneet</firstname><lastname>Gupta</lastname><nickname>vinni</nickname><marks>95</marks></student><student rollno = \"593\"><firstname>jasvir</firstname><lastname>singn</lastname><nickname>jazz</nickname><marks>90</marks></student></class>";
          String message = "<message extra=\"plz ignore\" sender=\"Anders\" ><disconnect /><text exxxtra=\"plz iiiignore\" color=\"#000000\">Hej &amp; <fetstil>på</fetstil> dig</text></message>";
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
