package uni.momotenko.module2.lab1.parsers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Parser {
    public Parser() {
        DocumentBuilderFactory documentBuilderFactory = null;
        DocumentBuilder documentBuilder = null;

        try{
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document document = null;

        try{
            document = documentBuilder.parse(new File("./objects/data.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        Element root = document.getDocumentElement();

        if (root.getTagName().equals("map")){
            NodeList daysList = root.getElementsByTagName("day");

            for (int i =0, length = daysList.getLength(); i < length; ++i){
                Element day = (Element) daysList.item(i);
                String dayYear = day.getAttribute("year");
                String dayMonth = day.getAttribute("month");
                String dayDay = day.getAttribute("day");

                System.out.printf("%s %s %s\n", dayYear, dayMonth, dayDay);

                NodeList eventsList = day.getElementsByTagName("event");
                for (int j =0, eventsListLength = eventsList.getLength(); j < eventsListLength; ++j){
                    Element event = (Element) eventsList.item(j);
                    String eventHours = event.getAttribute("hours");
                    String eventMinutes = event.getAttribute("minutes");
                    String eventDescription = event.getAttribute("decription");

                    System.out.printf("%s %s %s\n", eventHours, eventMinutes, eventDescription);
                }
            }
        }
    }
}
