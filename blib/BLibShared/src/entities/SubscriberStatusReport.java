package entities;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Saves the information about the status report
 */
public class SubscriberStatusReport implements Serializable {
    private int subscriberId;
    private String name;
    private LocalDate date;
    private LocalDate endDate;

    public SubscriberStatusReport(String name, int subscriberId, LocalDate date, LocalDate endDate) {
        this.name = name;
        this.subscriberId = subscriberId;
        this.date = date;
        this.endDate = endDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }
}
