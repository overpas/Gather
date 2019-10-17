package com.github.overpass.gather.di.profile

import com.github.overpass.gather.di.ComponentManager
import com.github.overpass.gather.di.profile.detail.ProfileDetailComponentManager

class ProfileComponentManager(
        profileComponent: ProfileComponent
) : ComponentManager<Unit, ProfileComponent>({ profileComponent }) {

    private var profileDetailComponentManager: ProfileDetailComponentManager? = null

    fun getDetailComponentManager(): ProfileDetailComponentManager = profileDetailComponentManager
            ?: ProfileDetailComponentManager(component!!.getDetailComponent())
                    .also { profileDetailComponentManager = it }
}