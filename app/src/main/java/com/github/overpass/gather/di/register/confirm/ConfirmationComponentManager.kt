package com.github.overpass.gather.di.register.confirm

import com.github.overpass.gather.di.ComponentManager

class ConfirmationComponentManager(
        creator: () -> ConfirmationComponent
) : ComponentManager<ConfirmationComponent>(creator)