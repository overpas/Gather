package com.github.overpass.gather.di.profile

import com.github.overpass.gather.di.ParentScope
import com.github.overpass.gather.di.image.ImageSourceModule
import com.github.overpass.gather.di.profile.detail.ProfileDetailComponent
import com.github.overpass.gather.ui.profile.ProfileActivity
import dagger.Subcomponent

@ParentScope
@Subcomponent(modules = [ProfileModule::class, ImageSourceModule::class])
interface ProfileComponent {

    fun inject(profileActivity: ProfileActivity)

    fun getDetailComponent(): ProfileDetailComponent
}