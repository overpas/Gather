package by.overpass.gather.di.map

import by.overpass.gather.di.ParentScope
import by.overpass.gather.di.map.detail.MapDetailComponent
import by.overpass.gather.ui.map.MapActivity
import dagger.Subcomponent

@ParentScope
@Subcomponent(modules = [
    MapModule::class,
    LocationPermissionModule::class
])
interface MapComponent {

    fun getDetailComponent(): MapDetailComponent

    fun inject(mapActivity: MapActivity)
}