package com.github.overpass.gather.di.map.detail

import com.github.overpass.gather.di.ComponentManager

class MapDetailComponentManager(
        creator: () -> MapDetailComponent
) : ComponentManager<MapDetailComponent>(creator)