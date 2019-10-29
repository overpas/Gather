package by.overpass.gather.di.register.add

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.auth.register.add.AddPersonalDataFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [AddPersonalDataModule::class])
interface AddPersonalDataComponent {

    fun inject(addPersonalDataFragment: AddPersonalDataFragment)
}