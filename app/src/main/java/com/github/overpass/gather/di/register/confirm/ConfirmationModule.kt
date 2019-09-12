package com.github.overpass.gather.di.register.confirm

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.auth.register.add.AddPersonalDataViewModel
import com.github.overpass.gather.screen.auth.register.confirm.ConfirmEmailViewModel
import com.github.overpass.gather.screen.map.detail.MapDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ConfirmationModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(ConfirmEmailViewModel::class)
    abstract fun bindConfirmEmailViewModel(viewModel: ConfirmEmailViewModel): ViewModel
}