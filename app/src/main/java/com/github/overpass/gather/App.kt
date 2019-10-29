package com.github.overpass.gather

import android.app.Application
import android.app.Service
import com.github.overpass.gather.di.app.ComponentManager
import com.github.overpass.gather.di.app.DaggerAppComponent
import com.github.overpass.gather.ui.base.BaseActivityKt
import com.github.overpass.gather.ui.base.BaseBottomSheetDialogFragment
import com.github.overpass.gather.ui.base.BaseDialogFragment
import com.github.overpass.gather.ui.base.BaseFragmentKt
import com.mapbox.mapboxsdk.Mapbox

class App : Application() {

    private lateinit var componentManager: ComponentManager

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDI()
        initMapbox()
    }

    private fun initDI() {
        componentManager = DaggerAppComponent.builder()
                .withContext(this)
                .withApp(this)
                .withMapboxToken(getString(R.string.mapbox_token))
                .withContentResolver(contentResolver)
                .build()
                .let { ComponentManager(it) }
    }

    private fun initMapbox() {
        Mapbox.getInstance(this, getString(R.string.mapbox_token))
    }

    companion object {

        private lateinit var instance: App

        val BaseActivityKt<*>.componentManager get() = instance.componentManager

        val BaseFragmentKt<*>.componentManager get() = instance.componentManager

        val BaseBottomSheetDialogFragment<*>.componentManager get() = instance.componentManager

        val BaseDialogFragment<*>.componentManager get() = instance.componentManager

        val Service.componentManager get() = instance.componentManager
    }
}