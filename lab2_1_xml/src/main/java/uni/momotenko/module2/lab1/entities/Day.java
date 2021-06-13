package uni.momotenko.module2.lab1.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day {
    private Integer year;
    private Integer month;
    private Integer day;
    private List<Event> events;

    public Day(Integer year, Integer month, Integer day, List<Event> events) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.events = events;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day1 = (Day) o;
        return year.equals(day1.year) && month.equals(day1.month) && day.equals(day1.day) && Objects.equals(events, day1.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day, events);
    }

    @Override
    public String toString() {
        return "Day{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", events=" + events +
                '}';
    }
}