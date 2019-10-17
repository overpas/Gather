package com.github.overpass.gather.di.register.signup

import com.github.overpass.gather.di.ComponentManager

class SignUpComponentManager(
        signUpComponent: SignUpComponent
) : ComponentManager<Unit, SignUpComponent>({ signUpComponent })