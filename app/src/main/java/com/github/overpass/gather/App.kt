package com.github.overpass.gather

import android.app.Application
import android.content.Context

import com.mapbox.mapboxsdk.Mapbox

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Mapbox.getInstance(this, getString(R.string.mapbox_token))
    }

    companion object {
        private lateinit var instance: App

        @JvmStatic
        fun getAppContext(): Context = instance.applicationContext
    }
}
