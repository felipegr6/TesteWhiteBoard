package br.com.fgr.testewhiteboard.model;

import java.io.Serializable;

public class Discipline implements Serializable {

    private String id;
    private String name;

    public Discipline(String id, String name) {

        this.id = id;
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
