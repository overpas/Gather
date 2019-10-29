package by.overpass.gather.di.login

import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.validator.ValidatorModule
import by.overpass.gather.ui.auth.login.LoginActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [SignInModule::class, ValidatorModule::class])
interface SignInComponent {

    fun inject(login: LoginActivity)
}