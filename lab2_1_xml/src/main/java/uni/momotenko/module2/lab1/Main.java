package uni.momotenko.module2.lab1;

import uni.momotenko.module2.lab1.entities.Notebook;
import uni.momotenko.module2.lab1.parsers.Parser;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        Notebook notebook = parser.parse("./objects/data.xml");
        if (notebook != null){
            System.out.println(notebook.toString());
        }
    }
}
