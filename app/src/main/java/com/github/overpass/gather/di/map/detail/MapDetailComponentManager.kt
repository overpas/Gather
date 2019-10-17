package com.github.overpass.gather.di.map.detail

import com.github.overpass.gather.di.ComponentManager

class MapDetailComponentManager(
        mapDetailComponent: MapDetailComponent
) : ComponentManager<Unit, MapDetailComponent>({ mapDetailComponent })