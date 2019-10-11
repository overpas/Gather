package com.github.overpass.gather.di.register.add

import com.github.overpass.gather.di.ComponentManager

class AddPersonalDataComponentManager(
        creator: () -> AddPersonalDataComponent
) : ComponentManager<AddPersonalDataComponent>(creator)