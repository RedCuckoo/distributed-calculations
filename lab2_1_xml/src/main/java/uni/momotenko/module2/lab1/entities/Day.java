package uni.momotenko.module2.lab1.entities;

import java.util.ArrayList;
import java.util.List;

public class Day {
    List<Event> events = new ArrayList<>();

    public Day() {
    }

    public Day(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event){
        events.add(event);
    }
}