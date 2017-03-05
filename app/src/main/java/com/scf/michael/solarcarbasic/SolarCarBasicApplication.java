package com.scf.michael.solarcarbasic;

/**
 * Created by Tom on 3/4/2017.
 */

import android.app.Application;
import android.content.res.Configuration;
import com.orm.SugarApp;
import com.orm.SugarContext;
import com.scf.michael.solarcarbasic.api.Auth;
import com.scf.michael.solarcarbasic.api.TeamLocation;

public class SolarCarBasicApplication extends SugarApp {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(getApplicationContext());
        Auth.findById(Auth.class, (long) 1);
        TeamLocation.findById(TeamLocation.class, (long) 1);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}