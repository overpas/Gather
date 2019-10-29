package by.overpass.gather.di.splash

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.splash.SplashScreenActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {

    fun inject(splashScreenActivity: SplashScreenActivity)
}