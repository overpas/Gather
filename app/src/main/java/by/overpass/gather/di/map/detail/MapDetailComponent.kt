package by.overpass.gather.di.map.detail

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.map.detail.MapFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [MapDetailModule::class, MapDetailsModule::class])
interface MapDetailComponent {

    fun inject(mapFragment: MapFragment)
}