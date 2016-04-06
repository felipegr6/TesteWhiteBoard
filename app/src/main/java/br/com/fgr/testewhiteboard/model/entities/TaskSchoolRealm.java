package br.com.fgr.testewhiteboard.model.entities;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TaskSchoolRealm extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String name;
    @Required
    private Date date;

    private RealmList<ImageRealm> images;
    private DisciplineRealm discipline;
    private double grade;
    private boolean done;
    private long idCalendar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DisciplineRealm getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineRealm discipline) {
        this.discipline = discipline;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public long getIdCalendar() {
        return idCalendar;
    }

    public void setIdCalendar(long idCalendar) {
        this.idCalendar = idCalendar;
    }

    public RealmList<ImageRealm> getImages() {
        return images;
    }

    public void setImages(RealmList<ImageRealm> images) {
        this.images = images;
    }

}