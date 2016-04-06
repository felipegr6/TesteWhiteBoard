package br.com.fgr.testewhiteboard.model.calendar;

import org.joda.time.DateTime;

import java.util.TimeZone;

public class CalendarItem {

    private String name;
    private String discipline;
    private DateTime startDate;
    private DateTime endDate;

    public CalendarItem(String name, String discipline, DateTime startDate, DateTime endDate) {

        this.name = name;
        this.discipline = discipline;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public String getTitle() {
        return String.format("%s - %s", name, discipline);
    }

    public String getDescription() {
        return String.format("Atividade %s de %s", name, discipline);
    }

    public long getStartDate() {
        return startDate.getMillis();
    }

    public long getEndDate() {
        return endDate.getMillis();
    }

    public int hasAlarm() {
        return 1;
    }

    public String getTimeZone() {
        return TimeZone.getAvailableIDs()[196];
    }

}