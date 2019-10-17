package com.github.overpass.gather.di.profile.detail

import com.github.overpass.gather.di.ComponentManager

class ProfileDetailComponentManager(
        profileDetailComponent: ProfileDetailComponent
): ComponentManager<Unit, ProfileDetailComponent>({ profileDetailComponent })