package br.com.fgr.testewhiteboard.ui;

import java.util.Date;
import java.util.List;

public interface TaskActionsMVP {

    // Presenter -> View
    interface RequiredViewOperations {

        void populateDisciplines(List<String> disciplines);

        void goToMainActivity();

        void onError(String message);

    }

    // View -> Presenter
    interface PresenterOperations {

        void retrieveDisciplines();

        void addDiscipline(String discipline);

        void addTask(String name, String discipline, Date date, double grade, boolean isDone);

    }

    // Model -> Presenter
    interface RequiredPresenterOperations {

        void onSuccessAddTask();

        void onSuccessDisciplines(List<String> disciplines);

        void onError(String message);

    }

    // Presenter -> Model
    interface ModelOperations {

        void retrieveDisciplines();

        void addDiscipline(String discipline);

        void addTask(String name, String discipline, Date date, double grade, boolean isDone);

    }

}