package com.github.overpass.gather.screen.map

import android.os.Bundle
import com.github.overpass.gather.App.Companion.appComponentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.di.map.MapComponent
import com.github.overpass.gather.di.map.MapComponentManager
import com.github.overpass.gather.model.commons.FragmentUtils
import com.github.overpass.gather.screen.base.BackPressActivity
import com.github.overpass.gather.screen.map.detail.MapFragment

class MapActivity : BackPressActivity<MapViewModel, MapComponent>() {

    override val componentManager: MapComponentManager =
            appComponentManager.getMapComponentManager()

    override fun createComponent(): MapComponent = componentManager.getOrCreate(Unit)

    override fun onComponentCreated(component: MapComponent) {
        component.inject(this)
    }

    override val layoutRes: Int = R.layout.activity_map

    override fun createViewModel(): MapViewModel {
        return viewModelProvider.get(MapViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            FragmentUtils.replace(supportFragmentManager, R.id.flMapFragmentContainer,
                    MapFragment.newInstance(), false)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
