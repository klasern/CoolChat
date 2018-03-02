/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coolChat;

/**
 *
 * @author Anders
 */
import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public final class XmlHandler {

    private XmlHandler() {
        throw new IllegalStateException("Do not instantiate this class.");
    }

    /**
     * Reades the xmlMessage and creates a ChatTextLine with relevant info.
     *
     * @param xmlMessage
     * @return
     */
    public static ChatTextLine readXml(String xmlMessage) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        ChatTextLine output = new ChatTextLine();
        try {
            reader = inputFactory.createXMLStreamReader(new StringReader(xmlMessage));
            output = makeChatTextLine(reader);
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
            output.setBrokenXml(true);
            return output;
        }
        return output;
    }

    /* Creates the ChatTextLine corresponding to the given xml */
    private static ChatTextLine makeChatTextLine(XMLStreamReader reader)
            throws XMLStreamException {

        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("message")) {
                        return readMessage(reader);
                    } else if (elementName.equals("request")) {
                        return readRequest(reader);
                    } else {
                        unknownStartTag(reader, elementName);
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
            }
        }
        /* Only reached if reader is empty and never found a message or request
        tag.
         */
        throw new XMLStreamException("Premature end of file");
    }

    /* Called when an unknown start tag is reached. */
    private static void unknownStartTag(XMLStreamReader reader,
            String elementName) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.END_ELEMENT:
                    String endTagName = reader.getLocalName();
                    if (endTagName.equals(elementName)) {
                        return;
                    }
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /* Called when a <message> start tag is reached. */
    private static ChatTextLine readMessage(XMLStreamReader reader)
            throws XMLStreamException {
        ChatTextLine outputMsg = new ChatTextLine();
        String name;
        boolean textOrDiscTag = false;

        name = reader.getAttributeValue(null, "sender");
        if (name == null) {
            name = "Name not found";
        }
        outputMsg.setName(name);

        outputMsg.setReplyNo(false);
        outputMsg.setRequest(false);

        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("text")) {
                        textOrDiscTag = true;
                        outputMsg.setTextColor(readColor(reader));
                        outputMsg.setMessage(readText(reader));
                        /* If text, it's not disconnect */
                        outputMsg.setDisconnectMessage(false);
                    } else if (elementName.equals("disconnect")) {
                        textOrDiscTag = true;
                        outputMsg.setDisconnectMessage(true);
                    } else {
                        unknownStartTag(reader, elementName);
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (textOrDiscTag == false) {
                        throw new XMLStreamException("No text or disconnect "
                                + "tag");
                    }
                    return outputMsg;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /* Called when a <request> start tag is reached. */
    private static ChatTextLine readRequest(XMLStreamReader reader)
            throws XMLStreamException {
        ChatTextLine outputMsg;
        outputMsg = new ChatTextLine();
        String reply;
        reply = reader.getAttributeValue(null, "reply");
        outputMsg.setRequest(true);
        if (reply != null) {
            if (reply.equals("no")) {
                outputMsg.setReplyNo(true);
            } else {
                outputMsg.setReplyNo(false);
            }
        } else {
            outputMsg.setReplyNo(false);
        }
        /* Read the messagetext */
        outputMsg.setMessage(readText(reader));

        return outputMsg;
    }

    /* Gets the color attribute from <text> start tag */
    private static Color readColor(XMLStreamReader reader) {
        Color c;
        String color;
        color = reader.getAttributeValue(null, "color");
        /* If no color chosen, use black text. */
        if (color == null) {
            color = "#000000";
        }
        /* convert RGB to color-object */
        c = Color.decode(color);
        return c;
    }

    /* Called when a <text> start tag is reached. */
    private static String readText(XMLStreamReader reader)
            throws XMLStreamException {
        StringBuilder message = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                    message.append(reader.getText());
                    break;
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("fetstil")) {
                        message.append(readBold(reader));
                    } else if (elementName.equals("kursiv")) {
                        message.append(readItalic(reader));
                    } else {
                        unknownStartTag(reader, elementName);
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return message.toString();
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /* Called when a <fetstil> start tag is reached. */
    private static String readBold(XMLStreamReader reader)
            throws XMLStreamException {
        StringBuilder message = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                    message.append(reader.getText());
                    break;
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("kursiv")) {
                        message.append(readItalic(reader));
                    } else {
                        unknownStartTag(reader, elementName);
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return message.toString();
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /* Called when a <kursiv> start tag is reached. */
    private static String readItalic(XMLStreamReader reader)
            throws XMLStreamException {
        StringBuilder message = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                    message.append(reader.getText());
                    break;
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("fetstil")) {
                        message.append(readBold(reader));
                    } else {
                        unknownStartTag(reader, elementName);
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return message.toString();
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    /**
     * Writes XML with the given name, color, and message text.
     *
     * @param name
     * @param textColor
     * @param text
     * @return
     */
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

    /**
     * Creates disconnectmessage.
     *
     * @param nameIn
     * @return
     */
    public static String disconnectMessage(String nameIn) {
        return "<message sender=\"" + nameIn + "\"><disconnect /></message>";
    }

    /**
     * Creates "no" request tag pair.
     *
     * @return
     */
    public static String requestNoMessage() {
        return "<request reply=\"no\"></request>";
    }

    /**
     * Creates request tag.
     *
     * @param message
     * @return
     */
    public static String requestMessage(String message) {
        return "<request>" + message + "</request>";
    }

}
