package by.overpass.gather.di.profile

import by.overpass.gather.di.ParentScope
import by.overpass.gather.di.image.ImageSourceModule
import by.overpass.gather.di.profile.detail.ProfileDetailComponent
import by.overpass.gather.ui.profile.ProfileActivity
import dagger.Subcomponent

@ParentScope
@Subcomponent(modules = [ProfileModule::class, ImageSourceModule::class])
interface ProfileComponent {

    fun inject(profileActivity: ProfileActivity)

    fun getDetailComponent(): ProfileDetailComponent
}