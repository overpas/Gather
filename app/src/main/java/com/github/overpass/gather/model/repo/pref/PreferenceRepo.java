package com.github.overpass.gather.model.repo.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.overpass.gather.screen.splash.StartStatus;

public class PreferenceRepo {

    private static final String START_STATUS_KEY = "START_STATUS_KEY";

    private final Context context;
    private SharedPreferences preferences;

    public PreferenceRepo(Context context) {
        this.context = context.getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setStartStatus(StartStatus startStatus) {
        preferences.edit()
                .putInt(START_STATUS_KEY, startStatus.ordinal())
                .apply();
    }

    public StartStatus getStartStatus() {
        int status = preferences.getInt(START_STATUS_KEY, StartStatus.UNAUTHORIZED.ordinal());
        if (status == StartStatus.AUTHORIZED.ordinal()) {
            return StartStatus.AUTHORIZED;
        } else if (status == StartStatus.UNAUTHORIZED.ordinal()) {
            return StartStatus.UNAUTHORIZED;
        } else if (status == StartStatus.UNCONFIRMED_EMAIL.ordinal()) {
            return StartStatus.UNCONFIRMED_EMAIL;
        } else {
            return StartStatus.NOT_ADDED_DATA;
        }
    }
}
