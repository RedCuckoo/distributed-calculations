package uni.momotenko.module2.lab1.parsers;

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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    public Notebook parse(String filename){
        DocumentBuilderFactory documentBuilderFactory = null;
        DocumentBuilder documentBuilder = null;

        try{
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);

            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setErrorHandler(new ParserErrorHandler());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }

        Document document = null;

        try{
            document = documentBuilder.parse(new File(filename));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            return null;
        }

        Element root = document.getDocumentElement();

        Notebook notebook = new Notebook();

        if (root.getTagName().equals("map")){
            NodeList daysList = root.getElementsByTagName("day");

            for (int i =0, length = daysList.getLength(); i < length; ++i){
                Element day = (Element) daysList.item(i);
                String dayYear = day.getAttribute("year");
                String dayMonth = day.getAttribute("month");
                String dayDay = day.getAttribute("day");

                Day dayEntity = new Day(Integer.parseInt(dayYear),Integer.parseInt(dayMonth), Integer.parseInt(dayDay), new ArrayList<>());

                NodeList eventsList = day.getElementsByTagName("event");
                for (int j =0, eventsListLength = eventsList.getLength(); j < eventsListLength; ++j){
                    Element event = (Element) eventsList.item(j);
                    String eventHours = event.getAttribute("hours");
                    String eventMinutes = event.getAttribute("minutes");
                    String eventDescription = event.getAttribute("description");

                    Event eventEntity = new Event(Integer.parseInt(eventHours),Integer.parseInt(eventMinutes), eventDescription);
                    dayEntity.addEvent(eventEntity);
                }

                notebook.addDay(dayEntity);
            }

            return notebook;
        }

        return null;
    }
}
