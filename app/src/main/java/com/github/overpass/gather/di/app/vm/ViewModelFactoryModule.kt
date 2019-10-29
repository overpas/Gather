package com.github.overpass.gather.di.app.vm

import androidx.lifecycle.ViewModelProvider
import com.github.overpass.gather.screen.base.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}