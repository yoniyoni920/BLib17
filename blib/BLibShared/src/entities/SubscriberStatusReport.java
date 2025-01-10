package entities;

import java.time.LocalDate;

/**
 * This class tracks the status of some subscriber.
 * In truth this class is more like a freeze status report as it only reports frozen subs
 * It's possible this will be changed in the future as reports become clearer.
 */
public class SubscriberStatusReport {
    private LocalDate reportDate;
    private LocalDate freezeDate;
    private LocalDate freezeEndDate;

    public SubscriberStatusReport(LocalDate reportDate, LocalDate freezeDate, LocalDate freezeEndDate) {
        this.reportDate = reportDate;
        this.freezeDate = freezeDate;
        this.freezeEndDate = freezeEndDate;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public LocalDate getFreezeDate() {
        return freezeDate;
    }

    public LocalDate getFreezeEndDate() {
        return freezeEndDate;
    }
}
