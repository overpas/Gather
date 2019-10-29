package by.overpass.gather.di.profile

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ParentScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.profile.GeneralProfileViewModel
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