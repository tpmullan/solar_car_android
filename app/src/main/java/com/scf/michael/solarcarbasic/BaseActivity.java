package com.scf.michael.solarcarbasic;

import android.app.Activity;
import android.os.Bundle;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by tom on 2/9/17.
 */

public class BaseActivity extends Activity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);

        realm = Realm.getDefaultInstance();


        //RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        //Realm.setDefaultConfiguration(realmConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public Realm getRealm() {
        return realm;
    }
}