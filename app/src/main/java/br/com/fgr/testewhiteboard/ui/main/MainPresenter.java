package br.com.fgr.testewhiteboard.ui.main;

import java.lang.ref.WeakReference;
import java.util.List;

import br.com.fgr.testewhiteboard.model.TaskSchool;

public class MainPresenter implements MainActionsMVP.PresenterOperations,
        MainActionsMVP.RequiredPresenterOperations {

    private WeakReference<MainActionsMVP.RequiredViewOperations> view;
    private MainActionsMVP.ModelOperations model;

    public MainPresenter(MainActionsMVP.RequiredViewOperations view) {

        this.view = new WeakReference<>(view);
        this.model = new MainModel(this);

    }

    @Override
    public void retrieveTasks() {
        model.retrieveTasks();
    }

    @Override
    public void onSuccessTasks(List<TaskSchool> tasks) {
        view.get().populateDisciplines(tasks);
    }

    @Override
    public void onError(String message) {
        view.get().onError(message);
    }

}