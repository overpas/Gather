package com.github.overpass.gather.di.login

import com.github.overpass.gather.di.ComponentManager

class SignInComponentManager(
        creator: () -> SignInComponent
) : ComponentManager<SignInComponent>(creator)