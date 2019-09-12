package com.github.overpass.gather.di.map.detail

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.screen.map.detail.MapFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [MapDetailModule::class])
interface MapDetailComponent {

    fun inject(mapFragment: MapFragment)
}