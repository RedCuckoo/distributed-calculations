package uni.momotenko.module2.lab1;

import org.xml.sax.SAXException;
import uni.momotenko.module2.lab1.entities.Notebook;
import uni.momotenko.module2.lab1.parsers.Parser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Parser parser = new Parser();
            Notebook notebook = parser.parse("./objects/data.xml");
            if (notebook != null) {
                System.out.println(notebook.toString());
                parser.writeToXML("./objects/result.xml", notebook);
            }
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            System.out.println("Something bad happened while parsing");
            e.printStackTrace();
        }
    }
}
