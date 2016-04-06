package br.com.fgr.testewhiteboard.model;

import java.io.Serializable;

public class Image implements Serializable {

    private String image;

    public Image(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

}
