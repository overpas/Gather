package com.github.overpass.gather.screen.map

import android.os.Bundle

import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.FragmentUtils
import com.github.overpass.gather.screen.base.BackPressActivity
import com.github.overpass.gather.screen.map.detail.MapFragment

class MapActivity : BackPressActivity<MapViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_map
    }

    override fun createViewModel(): MapViewModel {
        return viewModelProvider.get(MapViewModel::class.java)
    }

    override fun inject() {
        componentManager.getMapComponent()
                .inject(this)
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
