package com.github.overpass.gather.di.profile.detail

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.ui.profile.ProfileFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [ProfileDetailModule::class])
interface ProfileDetailComponent {

    fun inject(profileFragment: ProfileFragment)
}