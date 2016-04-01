package br.com.fgr.testewhiteboard.application;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TestApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        JodaTimeAndroid.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }

}