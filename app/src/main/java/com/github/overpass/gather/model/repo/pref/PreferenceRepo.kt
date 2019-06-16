package com.github.overpass.gather.model.repo.pref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.github.overpass.gather.screen.splash.StartStatus

class PreferenceRepo(context: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var startStatus: StartStatus
        get() {
            return when (preferences.getInt(START_STATUS_KEY, StartStatus.UNAUTHORIZED.ordinal)) {
                StartStatus.AUTHORIZED.ordinal -> StartStatus.AUTHORIZED
                StartStatus.UNAUTHORIZED.ordinal -> StartStatus.UNAUTHORIZED
                StartStatus.UNCONFIRMED_EMAIL.ordinal -> StartStatus.UNCONFIRMED_EMAIL
                else -> StartStatus.NOT_ADDED_DATA
            }
        }
        set(startStatus) = preferences.edit()
                .putInt(START_STATUS_KEY, startStatus.ordinal)
                .apply()

    companion object {
        private const val START_STATUS_KEY = "START_STATUS_KEY"
    }
}
