package com.github.overpass.gather.di.login

import com.github.overpass.gather.di.ComponentManager

class SignInComponentManager(
        signInComponent: SignInComponent
) : ComponentManager<Unit, SignInComponent>({ signInComponent })