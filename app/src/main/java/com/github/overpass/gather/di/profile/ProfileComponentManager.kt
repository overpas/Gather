package com.github.overpass.gather.di.profile

import com.github.overpass.gather.di.ComponentManager
import com.github.overpass.gather.di.profile.detail.ProfileDetailComponentManager

class ProfileComponentManager(
        creator: () -> ProfileComponent
) : ComponentManager<ProfileComponent>(creator) {

    private var profileDetailComponentManager: ProfileDetailComponentManager? = null

    fun getDetailComponentManager(): ProfileDetailComponentManager =
            profileDetailComponentManager ?: ProfileDetailComponentManager { get().getDetailComponent() }
                    .also { profileDetailComponentManager = it }
}