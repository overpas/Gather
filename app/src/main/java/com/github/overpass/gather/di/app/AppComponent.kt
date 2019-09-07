package com.github.overpass.gather.di.app

import android.content.Context
import com.github.overpass.gather.di.app.vm.ViewModelFactoryModule
import com.github.overpass.gather.di.login.SignInComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class, ViewModelFactoryModule::class])
interface AppComponent {

    fun signIn(): SignInComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}