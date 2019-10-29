package com.github.overpass.gather.di.map.detail

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.ui.map.detail.MapDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MapDetailModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(MapDetailViewModel::class)
    abstract fun bindMapDetailViewModel(mapDetailViewModel: MapDetailViewModel): ViewModel
}