package br.com.fgr.testewhiteboard.ui.task;

import android.content.Context;

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

        void addTask(Context context, String id, String name, String discipline, Date date,
                     double grade, boolean isDone);

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

        void addTask(Context context, String id, String name, String discipline, Date date,
                     double grade, boolean isDone);

    }

}