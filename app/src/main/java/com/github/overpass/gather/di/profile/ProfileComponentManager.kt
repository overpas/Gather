package com.github.overpass.gather.di.profile

import com.github.overpass.gather.di.profile.detail.ProfileDetailComponent
import com.github.overpass.gather.screen.profile.ProfileActivity

class ProfileComponentManager(private val profileComponent: ProfileComponent): ProfileComponent {

    private var profileDetailComponent: ProfileDetailComponent? = null

    override fun inject(profileActivity: ProfileActivity) = profileComponent.inject(profileActivity)

    override fun getDetailComponent(): ProfileDetailComponent =
            profileDetailComponent ?: profileComponent.getDetailComponent()
                    .also { profileDetailComponent = it }

    fun clearDetailComponent() {
        profileDetailComponent = null
    }
}