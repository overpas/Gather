package com.github.overpass.gather

import android.app.Application
import com.github.overpass.gather.di.ComponentManager
import com.github.overpass.gather.di.app.AppComponentManager
import com.github.overpass.gather.di.app.DaggerAppComponent
import com.github.overpass.gather.screen.base.*
import com.mapbox.mapboxsdk.Mapbox

class App : Application() {

    private lateinit var appComponentManager: AppComponentManager

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDI()
        initMapbox()
    }

    private fun initDI() {
        appComponentManager = DaggerAppComponent.factory()
                .create(this, this, getString(R.string.mapbox_token), contentResolver)
                .let { AppComponentManager(it) }
    }

    private fun initMapbox() {
        Mapbox.getInstance(this, getString(R.string.mapbox_token))
    }

    companion object {

        private lateinit var instance: App

        val BaseActivityKt<*>.appComponentManager get() = instance.appComponentManager

        val BaseActivity<*, *>.appComponentManager get() = instance.appComponentManager

        val BaseFragmentKt<*>.appComponentManager get() = instance.appComponentManager

        val BaseFragment<*, *>.appComponentManager get() = instance.appComponentManager

        val BaseBottomSheetDialogFragment<*, *>.appComponentManager get() = instance.appComponentManager

        val BaseDialogFragment<*, *>.appComponentManager get() = instance.appComponentManager

        val ComponentManager<*, *>.appComponentManager get() = instance.appComponentManager
    }
}