package com.github.overpass.gather.di.register.signup

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.auth.register.add.AddPersonalDataViewModel
import com.github.overpass.gather.screen.auth.register.confirm.ConfirmEmailViewModel
import com.github.overpass.gather.screen.auth.register.signup.SignUpViewModel
import com.github.overpass.gather.screen.map.detail.MapDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SignUpModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindSignUpViewModel(viewModel: SignUpViewModel): ViewModel
}