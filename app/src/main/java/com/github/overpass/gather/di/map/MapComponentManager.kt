package com.github.overpass.gather.di.map

import com.github.overpass.gather.di.ComponentManager
import com.github.overpass.gather.di.map.detail.MapDetailComponentManager

class MapComponentManager(
        mapComponent: MapComponent
) : ComponentManager<Unit, MapComponent>({ mapComponent }) {

    private var mapDetailComponentManager: MapDetailComponentManager? = null

    fun getDetailComponentManager(): MapDetailComponentManager = mapDetailComponentManager
            ?: MapDetailComponentManager(getOrCreate(Unit).getDetailComponent())
                    .also { mapDetailComponentManager = it }
}