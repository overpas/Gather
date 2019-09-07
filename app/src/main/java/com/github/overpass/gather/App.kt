package com.github.overpass.gather

import android.app.Application
import android.content.Context
import com.github.overpass.gather.di.app.AppComponent
import com.github.overpass.gather.di.app.DaggerAppComponent
import com.github.overpass.gather.screen.base.BaseActivityKt
import com.github.overpass.gather.screen.base.BaseViewModel
import com.mapbox.mapboxsdk.Mapbox

class App : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDI()
        initMapbox()
    }

    private fun initDI() {
        appComponent = DaggerAppComponent.factory()
                .create(this)
    }

    private fun initMapbox() {
        Mapbox.getInstance(this, getString(R.string.mapbox_token))
    }

    companion object {

        private lateinit var instance: App

        @JvmStatic
        fun getAppContext(): Context = instance.applicationContext

        val BaseActivityKt<*>.appComponent get() = instance.appComponent

        val BaseViewModel.appComponent get() = instance.appComponent
    }
}