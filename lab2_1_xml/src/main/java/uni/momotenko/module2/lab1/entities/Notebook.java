package uni.momotenko.module2.lab1.entities;

import java.util.ArrayList;
import java.util.List;

public class Notebook {
    private List<Day> days = new ArrayList<>();

    public Notebook() {
    }

    public Notebook(List<Day> days) {
        this.days = days;
    }

    public List<Day> getDays() {
        return days;
    }

    public void addDay(Day day){
        days.add(day);
    }

}
