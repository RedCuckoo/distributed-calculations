package uni.momotenko.module2.lab1.parsers;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uni.momotenko.module2.lab1.entities.Day;
import uni.momotenko.module2.lab1.entities.Event;
import uni.momotenko.module2.lab1.entities.Notebook;
import uni.momotenko.module2.lab1.parsers.errors.ParserErrorHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    DocumentBuilderFactory documentBuilderFactory;
    DocumentBuilder documentBuilder;

    public Parser() throws ParserConfigurationException {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(true);

        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.setErrorHandler(new ParserErrorHandler());

    }

    public Notebook parse(String filename) throws IOException, SAXException {
        Document document = documentBuilder.parse(new File(filename));
        Element root = document.getDocumentElement();
        Notebook notebook = new Notebook();

        if (root.getTagName().equals("notebook")) {
            NodeList daysList = root.getElementsByTagName("day");

            for (int i = 0, length = daysList.getLength(); i < length; ++i) {
                Element day = (Element) daysList.item(i);
                String dayYear = day.getAttribute("year");
                String dayMonth = day.getAttribute("month");
                String dayDay = day.getAttribute("day");

                Day dayEntity = new Day(Integer.parseInt(dayYear), Integer.parseInt(dayMonth), Integer.parseInt(dayDay), new ArrayList<>());

                NodeList eventsList = day.getElementsByTagName("event");
                for (int j = 0, eventsListLength = eventsList.getLength(); j < eventsListLength; ++j) {
                    Element event = (Element) eventsList.item(j);
                    String eventHours = event.getAttribute("hours");
                    String eventMinutes = event.getAttribute("minutes");
                    String eventDescription = event.getAttribute("description");

                    Event eventEntity = new Event(Integer.parseInt(eventHours), Integer.parseInt(eventMinutes), eventDescription);
                    dayEntity.addEvent(eventEntity);
                }

                notebook.addDay(dayEntity);
            }

            return notebook;
        }

        return null;
    }

    public void writeToXML(String filename, Notebook notebook) throws TransformerException {
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("notebook");
        document.appendChild(root);

        List<Day> days = notebook.getDays();

        for (Day d : days){
            Element day = document.createElement("day");

            Attr yearAttr = document.createAttribute("year");
            Attr monthAttr = document.createAttribute("month");
            Attr dayAttr = document.createAttribute("day");

            yearAttr.setValue(d.getYear().toString());
            monthAttr.setValue(d.getMonth().toString());
            dayAttr.setValue(d.getMonth().toString());

            day.setAttributeNode(yearAttr);
            day.setAttributeNode(monthAttr);
            day.setAttributeNode(dayAttr);

            root.appendChild(day);

            List<Event> events = d.getEvents();

            for (Event e : events){
                Element event = document.createElement("event");

                Attr hoursAttr = document.createAttribute("hours");
                Attr minutesAttr = document.createAttribute("minutes");
                Attr descriptionAttr = document.createAttribute("description");

                hoursAttr.setValue(e.getHours().toString());
                minutesAttr.setValue(e.getMinutes().toString());
                descriptionAttr.setValue(e.getDescription());

                event.setAttributeNode(hoursAttr);
                event.setAttributeNode(minutesAttr);
                event.setAttributeNode(descriptionAttr);

                day.appendChild(event);
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(filename));
        transformer.transform(domSource, streamResult);
    }
}
