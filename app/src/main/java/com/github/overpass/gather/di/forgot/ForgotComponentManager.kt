package com.github.overpass.gather.di.forgot

import com.github.overpass.gather.di.ComponentManager

class ForgotComponentManager(
        forgotComponent: ForgotComponent
) : ComponentManager<Unit, ForgotComponent>({ forgotComponent })