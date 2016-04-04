package br.com.fgr.testewhiteboard.ui.task;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import br.com.fgr.testewhiteboard.model.GenerateHashCode;
import br.com.fgr.testewhiteboard.model.entities.DisciplineRealm;
import br.com.fgr.testewhiteboard.model.entities.TaskSchoolRealm;
import io.realm.Realm;
import io.realm.RealmResults;

public class TaskModel implements TaskActionsMVP.ModelOperations {

    private TaskActionsMVP.RequiredPresenterOperations presenter;

    public TaskModel(TaskActionsMVP.RequiredPresenterOperations presenter) {
        this.presenter = presenter;
    }

    @Override
    public void retrieveDisciplines() {

        Realm realm = Realm.getDefaultInstance();

        List<String> disciplines = new ArrayList<>();
        RealmResults<DisciplineRealm> results = realm.where(DisciplineRealm.class)
                .findAllSorted("name");

        for (DisciplineRealm d : results)
            disciplines.add(d.getName());

        presenter.onSuccessDisciplines(disciplines);

    }

    @Override
    public void addDiscipline(final String discipline) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {

                try {

                    DisciplineRealm disc = realm.createObject(DisciplineRealm.class);

                    disc.setId(GenerateHashCode.hashCode(discipline));
                    disc.setName(discipline);

                } catch (Exception e) {
                    presenter.onError(e.getMessage() + "");
                }

            }

        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                retrieveDisciplines();
            }

        }, new Realm.Transaction.OnError() {

            @Override
            public void onError(Throwable error) {
                presenter.onError(error.getMessage() + "");
            }

        });

    }

    @Override
    public void addTask(final Context context, final String id, final String name,
                        final String discipline, final Date date, final double grade,
                        final boolean isDone) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {

                TaskSchoolRealm task = realm.where(TaskSchoolRealm.class).
                        equalTo("id", id).findFirst();
                DisciplineRealm disc;

                if (task == null) {

                    task = realm.createObject(TaskSchoolRealm.class);
                    disc = realm.where(DisciplineRealm.class)
                            .equalTo("name", discipline)
                            .findFirst();

                } else
                    disc = task.getDiscipline();

                try {

                    task.setId(GenerateHashCode.hashCode(name + discipline + date.toString()));
                    task.setName(name);
                    task.setDiscipline(disc);
                    task.setDate(date);
                    task.setGrade(grade);
                    task.setDone(isDone);

                    DateTime dt = new DateTime(date);
                    long startTime = dt.getMillis();
                    long endTime = dt.plusHours(1).getMillis();

                    ContentValues event = new ContentValues();

                    event.put(CalendarContract.Events.CALENDAR_ID, 1);
                    event.put(CalendarContract.Events.TITLE, String.format("%s - %s", name, discipline));
                    event.put(CalendarContract.Events.DESCRIPTION, String.format("Atividade %s de %s", name, discipline));
                    event.put(CalendarContract.Events.DTSTART, startTime);
                    event.put(CalendarContract.Events.DTEND, endTime);
                    event.put(CalendarContract.Events.HAS_ALARM, 1);
                    event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getAvailableIDs()[196]);

                    Uri url;

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {

                        ContentResolver cr = context.getContentResolver();
                        url = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, event);

                        if (url != null) {

                            Log.w("url", url.toString());

                            task.setIdCalendar(Long.parseLong(url.getLastPathSegment()));

                            for (int i = 1; i <= 7; i++) {

                                Uri REMINDERS_URI = Uri.parse("content://" + url.getHost() + "/reminders");
                                ContentValues values = new ContentValues();
                                values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(url.getLastPathSegment()));
                                values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                                values.put(CalendarContract.Reminders.MINUTES, i * 60 * 24);
                                cr.insert(REMINDERS_URI, values);

                            }

                        }

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    presenter.onError(e.getMessage() + "");

                }

            }

        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                presenter.onSuccessAddTask();
            }

        }, new Realm.Transaction.OnError() {

            @Override
            public void onError(Throwable error) {
                presenter.onError(error.getMessage());
            }

        });

    }

}