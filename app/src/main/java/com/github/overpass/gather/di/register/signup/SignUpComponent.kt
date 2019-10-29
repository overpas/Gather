package com.github.overpass.gather.di.register.signup

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.validator.ValidatorModule
import com.github.overpass.gather.ui.auth.register.signup.SignUpFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [SignUpModule::class, ValidatorModule::class])
interface SignUpComponent {

    fun inject(signUpFragment: SignUpFragment)
}