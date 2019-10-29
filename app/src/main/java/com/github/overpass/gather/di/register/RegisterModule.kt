package com.github.overpass.gather.di.register

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ParentScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.auth.register.RegisterViewModel
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