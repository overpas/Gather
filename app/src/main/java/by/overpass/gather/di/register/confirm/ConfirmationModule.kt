package by.overpass.gather.di.register.confirm

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.auth.register.confirm.ConfirmEmailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ConfirmationModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(ConfirmEmailViewModel::class)
    abstract fun bindConfirmEmailViewModel(viewModel: ConfirmEmailViewModel): ViewModel
}