package com.github.overpass.gather.di.forgot

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.validator.ValidatorModule
import com.github.overpass.gather.ui.auth.login.forgot.ForgotPasswordBottomFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [ForgotModule::class, ValidatorModule::class])
interface ForgotComponent {

    fun inject(forgotPasswordBottomFragment: ForgotPasswordBottomFragment)
}