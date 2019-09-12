package com.github.overpass.gather.di.app

import android.app.Application
import android.content.Context
import com.github.overpass.gather.di.app.modules.DispatcherModule
import com.github.overpass.gather.di.app.modules.FirebaseModule
import com.github.overpass.gather.di.app.vm.ViewModelFactoryModule
import com.github.overpass.gather.di.login.SignInComponent
import com.github.overpass.gather.di.map.MapComponent
import com.github.overpass.gather.di.profile.ProfileComponent
import com.github.overpass.gather.di.register.RegisterComponent
import com.github.overpass.gather.di.splash.SplashComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    FirebaseModule::class,
    ViewModelFactoryModule::class,
    DispatcherModule::class
])
interface AppComponent {

    fun getSignInComponent(): SignInComponent

    fun getMapComponent(): MapComponent

    fun getProfileComponent(): ProfileComponent

    fun getRegisterComponentFactory(): RegisterComponent.Factory

    fun getSplashComponent(): SplashComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context, @BindsInstance app: Application): AppComponent
    }
}