package com.github.overpass.gather.di.register.add

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.screen.auth.register.add.AddPersonalDataFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [AddPersonalDataModule::class])
interface AddPersonalDataComponent {

    fun inject(addPersonalDataFragment: AddPersonalDataFragment)
}