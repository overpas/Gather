package by.overpass.gather.di.profile.detail

import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.personal.data.PersonalDataModule
import by.overpass.gather.di.validator.ValidatorModule
import by.overpass.gather.ui.profile.ProfileFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [ProfileDetailModule::class, PersonalDataModule::class, ValidatorModule::class])
interface ProfileDetailComponent {

    fun inject(profileFragment: ProfileFragment)
}