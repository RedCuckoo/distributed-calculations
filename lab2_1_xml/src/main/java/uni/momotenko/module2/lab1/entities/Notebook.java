package uni.momotenko.module2.lab1.entities;

import java.util.ArrayList;
import java.util.List;

public class Notebook {
    private List<Day> days = new ArrayList<>();

    public Notebook() { }

    public Notebook(List<Day> days) {
        this.days = days;
    }

    public Day getDay(Integer year, Integer month, Integer day){
        for (Day d : days) {
            if (d.getYear().equals(year) && d.getMonth().equals(month) && d.getDay().equals(day)){
                return d;
            }
        }

        return null;
    }

    public List<Event> getEventsByDay(Day day){
        if (days.contains(day)){
            return days.get(days.indexOf(day)).getEvents();
        }

        return null;
    }

    public boolean addDay(Day day){
        if (!days.contains(day)){
            days.add(day);

            return true;
        }

        return false;
    }

    public boolean addEventByDay(Day day, Event event){
        if (days.contains(day)){
            days.get(days.indexOf(day)).addEvent(event);

            return true;
        }

        return false;
    }

}
