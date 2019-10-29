package by.overpass.gather.di.register.confirm

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.auth.register.confirm.ConfirmEmailFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [ConfirmationModule::class])
interface ConfirmationComponent {

    fun inject(confirmEmailFragment: ConfirmEmailFragment)
}