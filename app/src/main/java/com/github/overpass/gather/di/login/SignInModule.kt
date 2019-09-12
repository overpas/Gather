package com.github.overpass.gather.di.login

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.EMAIL_VALIDATOR
import com.github.overpass.gather.di.PASSWORD_VALIDATOR
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.model.data.validator.EmailValidator
import com.github.overpass.gather.model.data.validator.PasswordValidator
import com.github.overpass.gather.model.data.validator.Validator
import com.github.overpass.gather.screen.auth.login.SignInViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class SignInModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModel(signInViewModel: SignInViewModel): ViewModel
}