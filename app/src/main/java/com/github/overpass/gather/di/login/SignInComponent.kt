package com.github.overpass.gather.di.login

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.screen.auth.login.LoginActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [SignInViewModelModule::class])
interface SignInComponent {

    fun inject(login: LoginActivity)
}