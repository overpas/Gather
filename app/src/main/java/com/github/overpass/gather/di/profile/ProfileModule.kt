package com.github.overpass.gather.di.profile

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ParentScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.profile.GeneralProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileModule {

    @Binds
    @IntoMap
    @ParentScope
    @ViewModelKey(GeneralProfileViewModel::class)
    abstract fun bindGeneralProfileViewModel(generalProfileViewModel: GeneralProfileViewModel): ViewModel
}