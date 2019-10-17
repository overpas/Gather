package com.github.overpass.gather.di.closeup

import com.github.overpass.gather.di.ComponentManager

class CloseupComponentManager(
        closeupComponent: CloseupComponent
) : ComponentManager<Unit, CloseupComponent>({ closeupComponent })