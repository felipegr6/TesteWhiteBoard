package br.com.fgr.testewhiteboard.ui.main;

import java.util.List;

import br.com.fgr.testewhiteboard.model.TaskSchool;

public interface MainActionsMVP {

    // Presenter -> View
    interface RequiredViewOperations {

        void populateDisciplines(List<TaskSchool> tasks);

        void onError(String message);

    }

    // View -> Presenter
    interface PresenterOperations {

        void retrieveTasks();

    }

    // Model -> Presenter
    interface RequiredPresenterOperations {

        void onSuccessTasks(List<TaskSchool> tasks);

        void onError(String message);

    }

    // Presenter -> Model
    interface ModelOperations {

        void retrieveTasks();

    }

}