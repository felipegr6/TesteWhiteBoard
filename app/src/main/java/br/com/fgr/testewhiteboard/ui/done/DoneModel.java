package br.com.fgr.testewhiteboard.ui.done;

import java.util.ArrayList;
import java.util.List;

import br.com.fgr.testewhiteboard.model.Discipline;
import br.com.fgr.testewhiteboard.model.Image;
import br.com.fgr.testewhiteboard.model.TaskSchool;
import br.com.fgr.testewhiteboard.model.entities.ImageRealm;
import br.com.fgr.testewhiteboard.model.entities.TaskSchoolRealm;
import io.realm.Realm;
import io.realm.RealmResults;

public class DoneModel implements DoneActionsMVP.ModelOperations {

    private DoneActionsMVP.RequiredPresenterOperations presenter;

    public DoneModel(DoneActionsMVP.RequiredPresenterOperations presenter) {
        this.presenter = presenter;
    }

    @Override
    public void retrieveTasks() {

        List<TaskSchool> tasks = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TaskSchoolRealm> results = realm.where(TaskSchoolRealm.class)
                .equalTo("done", true).findAllSorted("date");

        for (TaskSchoolRealm ts : results) {

            List<Image> images = new ArrayList<>();

            for (ImageRealm ir : ts.getImages())
                images.add(new Image(ir.getImage()));

            tasks.add(new TaskSchool(ts.getId(), ts.getName(), ts.getDate(),
                    new Discipline(ts.getDiscipline().getId(), ts.getDiscipline().getName()),
                    ts.getGrade(), ts.isDone(), images));

        }

        presenter.onSuccessTasks(tasks);

    }

}