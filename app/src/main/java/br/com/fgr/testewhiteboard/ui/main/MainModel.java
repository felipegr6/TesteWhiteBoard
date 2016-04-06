package br.com.fgr.testewhiteboard.ui.main;

import java.util.ArrayList;
import java.util.List;

import br.com.fgr.testewhiteboard.model.Discipline;
import br.com.fgr.testewhiteboard.model.Image;
import br.com.fgr.testewhiteboard.model.TaskSchool;
import br.com.fgr.testewhiteboard.model.entities.ImageRealm;
import br.com.fgr.testewhiteboard.model.entities.TaskSchoolRealm;
import io.realm.Realm;

public class MainModel implements MainActionsMVP.ModelOperations {

    private MainActionsMVP.RequiredPresenterOperations presenter;

    public MainModel(MainActionsMVP.RequiredPresenterOperations presenter) {
        this.presenter = presenter;
    }

    @Override
    public void retrieveTasks() {

        Realm realm = Realm.getDefaultInstance();

        List<TaskSchool> tasks = new ArrayList<>();
        List<TaskSchoolRealm> results = realm.where(TaskSchoolRealm.class)
                .equalTo("done", false)
                .findAllSorted("date");

        for (TaskSchoolRealm r : results) {

            List<Image> images = new ArrayList<>();

            for (ImageRealm ir : r.getImages())
                images.add(new Image(ir.getImage()));

            tasks.add(new TaskSchool(r.getId(), r.getName(), r.getDate(),
                    new Discipline(r.getDiscipline().getId(), r.getDiscipline().getName()),
                    r.getGrade(), r.isDone(), images));

        }

        presenter.onSuccessTasks(tasks);

    }

}