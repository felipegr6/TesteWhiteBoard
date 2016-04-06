package br.com.fgr.testewhiteboard.model.entities;

import io.realm.RealmObject;

public class ImageRealm extends RealmObject {

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
