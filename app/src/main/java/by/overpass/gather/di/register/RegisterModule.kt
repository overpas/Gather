package by.overpass.gather.di.register

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ParentScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.auth.register.RegisterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RegisterModule {

    @Binds
    @IntoMap
    @ParentScope
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel
}