package com.github.overpass.gather.di.register.confirm

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.screen.auth.register.confirm.ConfirmEmailFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [ConfirmationModule::class])
interface ConfirmationComponent {

    fun inject(confirmEmailFragment: ConfirmEmailFragment)
}