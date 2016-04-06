package br.com.fgr.testewhiteboard.model.calendar;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NativeCalendar implements CalendarAbstract {

    private Context context;

    public NativeCalendar(Context context) {
        this.context = context;
    }

    @Override
    public long addCalendarItem(CalendarItem item) {

        long idCalendarItem = 0;
        ContentValues event;
        Uri url;

        event = mountContentValues(item);

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_CALENDAR) ==
                PackageManager.PERMISSION_GRANTED) {

            ContentResolver cr = context.getContentResolver();
            url = context.getContentResolver()
                    .insert(CalendarContract.Events.CONTENT_URI, event);

            if (url != null) {

                Log.w("url", url.toString());

                idCalendarItem = Long.parseLong(url.getLastPathSegment());

                Uri REMINDERS_URI = Uri.parse(String.format("content://%s/reminders", url.getHost()));

                for (ContentValues cv : mountReminders(idCalendarItem))
                    cr.insert(REMINDERS_URI, cv);

            }

        }

        return idCalendarItem;

    }

    @Override
    public long updateCalendarItem(long id, CalendarItem item) {

        deleteCalendarItem(id);

        return addCalendarItem(item);

    }

    @Override
    public void deleteCalendarItem(long id) {

        ContentResolver cr = context.getContentResolver();

        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
        int rows = cr.delete(deleteUri, null, null);
        Log.i("DeleteCalendar", "Rows deleted: " + rows);

    }


    private ContentValues mountContentValues(CalendarItem item) {

        ContentValues event = new ContentValues();

        event.put(CalendarContract.Events.CALENDAR_ID, 1);
        event.put(CalendarContract.Events.TITLE, item.getTitle());
        event.put(CalendarContract.Events.DESCRIPTION, item.getDescription());
        event.put(CalendarContract.Events.DTSTART, item.getStartDate());
        event.put(CalendarContract.Events.DTEND, item.getEndDate());
        event.put(CalendarContract.Events.HAS_ALARM, item.hasAlarm());
        event.put(CalendarContract.Events.EVENT_TIMEZONE, item.getTimeZone());

        return event;

    }

    private List<ContentValues> mountReminders(long id) {

        List<ContentValues> contentValues = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {

            ContentValues values = new ContentValues();

            values.put(CalendarContract.Reminders.EVENT_ID, id);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            values.put(CalendarContract.Reminders.MINUTES, i * 60 * 24);

            contentValues.add(values);

        }

        return contentValues;

    }

}