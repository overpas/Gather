package com.github.overpass.gather.di.map

import com.github.overpass.gather.di.map.detail.MapDetailComponent
import com.github.overpass.gather.screen.map.MapActivity

class MapComponentManager(private val mapComponent: MapComponent) : MapComponent {

    private var detailComponent: MapDetailComponent? = null

    override fun getDetailComponent(): MapDetailComponent =
            detailComponent ?: mapComponent.getDetailComponent()
                    .also { detailComponent = it }

    fun clearDetailComponent() {
        detailComponent = null
    }

    override fun inject(mapActivity: MapActivity) = mapComponent.inject(mapActivity)
}