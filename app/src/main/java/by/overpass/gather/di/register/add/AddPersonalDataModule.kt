package by.overpass.gather.di.register.add

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.auth.register.add.AddPersonalDataViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddPersonalDataModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(AddPersonalDataViewModel::class)
    abstract fun bindAddPersonalDataViewModel(viewModel: AddPersonalDataViewModel): ViewModel
}