package by.overpass.gather.di.profile.detail

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileDetailModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel
}
