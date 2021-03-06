package by.overpass.gather.ui.map

import android.os.Bundle
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.fragment.addToBackStack
import by.overpass.gather.commons.android.fragment.transaction
import by.overpass.gather.ui.base.BackPressActivity
import by.overpass.gather.ui.map.detail.MapFragment

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
