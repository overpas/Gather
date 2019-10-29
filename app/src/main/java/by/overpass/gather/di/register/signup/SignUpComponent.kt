package by.overpass.gather.di.register.signup

import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.validator.ValidatorModule
import by.overpass.gather.ui.auth.register.signup.SignUpFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [SignUpModule::class, ValidatorModule::class])
interface SignUpComponent {

    fun inject(signUpFragment: SignUpFragment)
}