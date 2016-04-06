package br.com.fgr.testewhiteboard.model.calendar;

public interface CalendarAbstract {

    long addCalendarItem(CalendarItem item);

    long updateCalendarItem(long id, CalendarItem item);

    void deleteCalendarItem(long id);

}