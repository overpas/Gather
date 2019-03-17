package com.github.overpass.gather;

import android.app.Application;
import android.content.Context;

import com.mapbox.mapboxsdk.Mapbox;

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Mapbox.getInstance(this, getString(R.string.mapbox_token));
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
