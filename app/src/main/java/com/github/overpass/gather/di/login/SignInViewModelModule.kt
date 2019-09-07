package com.github.overpass.gather.di.login

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.auth.login.SignInViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SignInViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModel(signInViewModel: SignInViewModel): ViewModel
}