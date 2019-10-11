package com.github.overpass.gather.di.map

import com.github.overpass.gather.di.ComponentManager
import com.github.overpass.gather.di.map.detail.MapDetailComponentManager

class MapComponentManager(
        creator: () -> MapComponent
) : ComponentManager<MapComponent>(creator) {

    private var mapDetailComponentManager: MapDetailComponentManager? = null

    fun getDetailComponentManager(): MapDetailComponentManager =
            mapDetailComponentManager ?: get().getDetailComponent()
                    .also { mapDetailComponentManager = MapDetailComponentManager { it } }
                    .let { mapDetailComponentManager!! }
}