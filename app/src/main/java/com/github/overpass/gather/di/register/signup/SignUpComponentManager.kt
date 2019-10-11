package com.github.overpass.gather.di.register.signup

import com.github.overpass.gather.di.ComponentManager

class SignUpComponentManager(
        creator: () -> SignUpComponent
) : ComponentManager<SignUpComponent>(creator)