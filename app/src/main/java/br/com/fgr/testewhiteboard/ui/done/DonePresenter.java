package br.com.fgr.testewhiteboard.ui.done;

import java.lang.ref.WeakReference;
import java.util.List;

import br.com.fgr.testewhiteboard.model.TaskSchool;

public class DonePresenter implements DoneActionsMVP.PresenterOperations,
        DoneActionsMVP.RequiredPresenterOperations {

    private WeakReference<DoneActionsMVP.RequiredViewOperations> view;
    private DoneActionsMVP.ModelOperations model;

    public DonePresenter(DoneActionsMVP.RequiredViewOperations view) {

        this.view = new WeakReference<>(view);
        this.model = new DoneModel(this);

    }

    @Override
    public void retrieveDoneTasks() {
        model.retrieveTasks();
    }

    @Override
    public void onSuccessTasks(List<TaskSchool> tasks) {
        view.get().populateDoneTasks(tasks);
    }

    @Override
    public void onError(String message) {
        view.get().onError(message);
    }

}