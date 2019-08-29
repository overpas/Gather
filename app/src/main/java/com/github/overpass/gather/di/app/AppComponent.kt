package com.github.overpass.gather.di.app

import android.content.Context
import com.github.overpass.gather.di.login.SignInComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun signIn(): SignInComponent
}