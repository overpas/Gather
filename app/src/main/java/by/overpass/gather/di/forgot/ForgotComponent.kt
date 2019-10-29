package by.overpass.gather.di.forgot

import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.validator.ValidatorModule
import by.overpass.gather.ui.auth.login.forgot.ForgotPasswordBottomFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [ForgotModule::class, ValidatorModule::class])
interface ForgotComponent {

    fun inject(forgotPasswordBottomFragment: ForgotPasswordBottomFragment)
}