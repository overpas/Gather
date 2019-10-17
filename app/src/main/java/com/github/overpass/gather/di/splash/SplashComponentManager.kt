package com.github.overpass.gather.di.splash

import com.github.overpass.gather.di.ComponentManager

class SplashComponentManager(
        splashComponent: SplashComponent
) : ComponentManager<Unit, SplashComponent>({ splashComponent })