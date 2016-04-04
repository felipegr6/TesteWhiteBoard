package br.com.fgr.testewhiteboard.ui.task;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

public class TaskPresenter implements TaskActionsMVP.PresenterOperations,
        TaskActionsMVP.RequiredPresenterOperations {

    private WeakReference<TaskActionsMVP.RequiredViewOperations> view;
    private TaskActionsMVP.ModelOperations model;

    public TaskPresenter(TaskActionsMVP.RequiredViewOperations view) {

        this.view = new WeakReference<>(view);
        this.model = new TaskModel(this);

    }

    @Override
    public void onSuccessAddTask() {
        view.get().goToMainActivity();
    }

    @Override
    public void onSuccessDisciplines(List<String> disciplines) {
        view.get().populateDisciplines(disciplines);
    }

    @Override
    public void onError(String message) {
        view.get().onError(message);
    }

    @Override
    public void retrieveDisciplines() {
        model.retrieveDisciplines();
    }

    @Override
    public void addDiscipline(String discipline) {
        model.addDiscipline(discipline);
    }


    @Override
    public void addTask(Context context, String id, String name, String discipline, Date date,
                        double grade, boolean isDone) {
        model.addTask(context, id, name, discipline, date, grade, isDone);
    }

}