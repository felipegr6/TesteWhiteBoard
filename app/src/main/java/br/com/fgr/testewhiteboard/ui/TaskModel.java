package br.com.fgr.testewhiteboard.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fgr.testewhiteboard.model.Discipline;
import br.com.fgr.testewhiteboard.model.GenerateHashCode;
import br.com.fgr.testewhiteboard.model.TaskSchool;
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
        RealmResults<Discipline> results = realm.where(Discipline.class).findAll();

        for (Discipline d : results)
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

                    Discipline disc = realm.createObject(Discipline.class);

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
    public void addTask(final String name, final String discipline, final Date date,
                        final double grade, final boolean isDone) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {

                TaskSchool task = realm.createObject(TaskSchool.class);
                Discipline disc = realm.where(Discipline.class)
                        .equalTo("name", discipline)
                        .findFirst();

                try {

                    task.setId(GenerateHashCode.hashCode(name));
                    task.setName(name);
                    task.setDiscipline(disc);
                    task.setDate(date);
                    task.setGrade(grade);
                    task.setDone(isDone);

                } catch (Exception e) {
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