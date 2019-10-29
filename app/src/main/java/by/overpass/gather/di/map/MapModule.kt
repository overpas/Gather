package by.overpass.gather.di.map

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ParentScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.map.MapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MapModule {

    @Binds
    @IntoMap
    @ParentScope
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(mapViewModel: MapViewModel): ViewModel
}