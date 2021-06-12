package uni.momotenko.module2.lab1.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Event {
    private Integer hours;
    private Integer minutes;
    private String description;

    public Event(Integer hours, Integer minutes, String description) {
        this.hours = hours;
        this.minutes = minutes;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return hours.equals(event.hours) && minutes.equals(event.minutes) && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes, description);
    }
}
