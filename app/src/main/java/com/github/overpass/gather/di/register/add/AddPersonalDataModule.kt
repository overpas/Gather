package com.github.overpass.gather.di.register.add

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.auth.register.add.AddPersonalDataViewModel
import com.github.overpass.gather.screen.map.detail.MapDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddPersonalDataModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(AddPersonalDataViewModel::class)
    abstract fun bindAddPersonalDataViewModel(viewModel: AddPersonalDataViewModel): ViewModel
}