package com.github.overpass.gather.di.profile.detail

import com.github.overpass.gather.di.ComponentManager

class ProfileDetailComponentManager(
        creator: () -> ProfileDetailComponent
): ComponentManager<ProfileDetailComponent>(creator)