package com.github.overpass.gather.di.login

import com.github.overpass.gather.di.validator.ValidatorModule
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.screen.auth.login.LoginActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [SignInModule::class, ValidatorModule::class])
interface SignInComponent {

    fun inject(login: LoginActivity)
}