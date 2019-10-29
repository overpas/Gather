package com.github.overpass.gather.di.splash

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.ui.splash.SplashScreenActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {

    fun inject(splashScreenActivity: SplashScreenActivity)
}