package com.github.overpass.gather.di.login

import com.github.overpass.gather.di.ViewModelScope
import com.github.overpass.gather.screen.auth.login.SignInViewModel
import dagger.Subcomponent

@ViewModelScope
@Subcomponent
interface SignInComponent {

    fun inject(signInViewModel: SignInViewModel)
}