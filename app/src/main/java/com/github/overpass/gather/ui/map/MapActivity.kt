package com.github.overpass.gather.ui.map

import android.os.Bundle
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.commons.android.fragment.addToBackStack
import com.github.overpass.gather.commons.android.fragment.transaction
import com.github.overpass.gather.ui.base.BackPressActivity
import com.github.overpass.gather.ui.map.detail.MapFragment

class MapActivity : BackPressActivity<MapViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_map
    }

    override fun createViewModel(): MapViewModel {
        return viewModelProvider.get(MapViewModel::class.java)
    }

    override fun inject() {
        componentManager.getMapComponent(lifecycle)
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            transaction()
                    .replace(R.id.flMapFragmentContainer, MapFragment.newInstance())
                    .addToBackStack(false)
                    .commit()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
