package com.github.overpass.gather.di.map

import com.github.overpass.gather.di.ParentScope
import com.github.overpass.gather.di.map.detail.MapDetailComponent
import com.github.overpass.gather.screen.map.MapActivity
import dagger.Subcomponent

@ParentScope
@Subcomponent(modules = [MapModule::class, LocationPermissionModule::class])
interface MapComponent {

    fun getDetailComponent(): MapDetailComponent

    fun inject(mapActivity: MapActivity)
}