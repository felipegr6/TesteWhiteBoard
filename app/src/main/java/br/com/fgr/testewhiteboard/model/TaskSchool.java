package br.com.fgr.testewhiteboard.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class TaskSchool implements Serializable {

    private String id;
    private String name;
    private Date date;
    private Discipline discipline;
    private double grade;
    private boolean done;
    private List<Image> images;

    public TaskSchool(String id, String name, Date date, Discipline discipline, double grade,
                      boolean done, List<Image> images) {

        this.id = id;
        this.name = name;
        this.date = date;
        this.discipline = discipline;
        this.grade = grade;
        this.done = done;
        this.images = images;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public String getGrade() {

        NumberFormat nf = new DecimalFormat("#0.00");

        return nf.format(grade);

    }

    public boolean isGradeZero() {
        return grade == 0.0;
    }

    public boolean isDone() {
        return done;
    }

    public List<Image> getImages() {
        return images;
    }

}