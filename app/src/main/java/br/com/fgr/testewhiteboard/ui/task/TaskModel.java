package br.com.fgr.testewhiteboard.ui.task;

import android.content.Context;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fgr.testewhiteboard.model.GenerateHashCode;
import br.com.fgr.testewhiteboard.model.calendar.CalendarAbstract;
import br.com.fgr.testewhiteboard.model.calendar.CalendarItem;
import br.com.fgr.testewhiteboard.model.calendar.NativeCalendar;
import br.com.fgr.testewhiteboard.model.entities.DisciplineRealm;
import br.com.fgr.testewhiteboard.model.entities.ImageRealm;
import br.com.fgr.testewhiteboard.model.entities.TaskSchoolRealm;
import io.realm.Realm;
import io.realm.RealmList;
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
    public void retrieveImages(String id) {

        Realm realm = Realm.getDefaultInstance();
        List<String> images = new ArrayList<>();
        TaskSchoolRealm task = realm.where(TaskSchoolRealm.class).equalTo("id", id).findFirst();

        for (ImageRealm ir : task.getImages())
            images.add(ir.getImage());

        presenter.onSuccessImages(images);

    }

    @Override
    public void addImage(String id, String image) {

        Realm realm = Realm.getDefaultInstance();

        TaskSchoolRealm taskSchool = realm.where(TaskSchoolRealm.class).equalTo("id", id).findFirst();
        RealmList<ImageRealm> images = taskSchool.getImages();

        realm.beginTransaction();

        ImageRealm imageRealm = new ImageRealm();
        imageRealm.setImage(image);

        images.add(imageRealm);

        realm.commitTransaction();

        retrieveImages(id);

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

                boolean isEdited;
                long idCalendarItem;

                if (task == null) {

                    task = realm.createObject(TaskSchoolRealm.class);
                    disc = realm.where(DisciplineRealm.class)
                            .equalTo("name", discipline)
                            .findFirst();
                    isEdited = false;

                } else {
                    disc = task.getDiscipline();
                    isEdited = true;
                }

                try {

                    task.setId(GenerateHashCode.hashCode(name + discipline + date.toString()));
                    task.setName(name);
                    task.setDiscipline(disc);
                    task.setDate(date);
                    task.setGrade(grade);
                    task.setDone(isDone);

                    DateTime startTime = new DateTime(date);
                    DateTime endTime = startTime.plusHours(1);

                    CalendarAbstract calendar = new NativeCalendar(context);
                    CalendarItem calendarItem = new CalendarItem(name, discipline, startTime, endTime);

                    if (isDone) {

                        calendar.deleteCalendarItem(task.getIdCalendar());
                        idCalendarItem = 0;

                    } else {

                        if (isEdited)
                            idCalendarItem = calendar.updateCalendarItem(task.getIdCalendar(), calendarItem);
                        else
                            idCalendarItem = calendar.addCalendarItem(calendarItem);

                    }

                    task.setIdCalendar(idCalendarItem);

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