package by.overpass.gather.di.app.vm

import androidx.lifecycle.ViewModelProvider
import by.overpass.gather.ui.base.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}