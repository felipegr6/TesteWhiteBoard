package br.com.fgr.testewhiteboard.ui.done;

import java.util.List;

import br.com.fgr.testewhiteboard.model.TaskSchool;

public interface DoneActionsMVP {

    // Presenter -> View
    interface RequiredViewOperations {

        void populateDoneTasks(List<TaskSchool> tasks);

        void goToMainActivity();

        void onError(String message);

    }

    // View -> Presenter
    interface PresenterOperations {

        void retrieveDoneTasks();

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