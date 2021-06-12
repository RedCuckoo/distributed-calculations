package uni.momotenko.module2.lab1.entities;

import java.time.LocalDateTime;

public class Event {
    private String description;
    private LocalDateTime dateAndTime;

    public Event(String description, LocalDateTime dateAndTime) {
        this.description = description;
        this.dateAndTime = dateAndTime;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }
}
