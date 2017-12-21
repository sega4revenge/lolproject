package com.sega.lolproject.services;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.sega.lolproject.model.Champion;

import java.util.ArrayList;

import io.realm.Realm;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Context application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }
    public static RealmController with(Context context) {

        if (instance == null) {
            instance = new RealmController(context);
        }
        return instance;
    }
    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Champion.class


    //find all objects in the Champion.class
    public ArrayList<Champion> getChampions() {

        return (ArrayList<Champion>) realm.copyFromRealm(realm.where(Champion.class).findAll());
    }

    //query a single item with the given id
    public Champion getChampion(String id) {

        return realm.where(Champion.class).equalTo("id", id).findFirst();
    }

    //check if Champion.class is empty


    //query example

}
