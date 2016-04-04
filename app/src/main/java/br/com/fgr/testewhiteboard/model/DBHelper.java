package br.com.fgr.testewhiteboard.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.fgr.testewhiteboard.model.entities.TaskSchoolRealm;
import io.realm.Realm;
import io.realm.RealmResults;

public final class DBHelper {

    private DBHelper() {

    }

    public static List<TaskSchool> getTasks() {

        List<TaskSchool> taskSchools = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();

        RealmResults<TaskSchoolRealm> results = realm.where(TaskSchoolRealm.class).findAll();

        for (TaskSchoolRealm task : results) {

            taskSchools.add(new TaskSchool(task.getId(), task.getName(), task.getDate(),
                    new Discipline(task.getDiscipline().getId(), task.getDiscipline().getName()),
                    task.getGrade(), task.isDone()));

        }

        return taskSchools;

    }

    public static int getIntegerId(String id) {

        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(id);

        while (matcher.find()) {
            sb.append(matcher.group());
        }

        return Integer.parseInt(sb.toString().substring(0, 6));

    }

}