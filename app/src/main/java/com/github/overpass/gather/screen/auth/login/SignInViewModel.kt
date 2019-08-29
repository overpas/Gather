package com.github.overpass.gather.screen.auth.login

import android.app.Application
import androidx.lifecycle.LiveData
import com.github.overpass.gather.App.Companion.appComponent
import com.github.overpass.gather.model.commons.SingleLiveEvent
import com.github.overpass.gather.model.commons.toLiveData
import com.github.overpass.gather.model.data.entity.signin.SignInStatus
import com.github.overpass.gather.model.usecase.login.SignInUseCase
import com.github.overpass.gather.screen.base.BaseViewModel
import javax.inject.Inject

class SignInViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    internal lateinit var signInUseCase: SignInUseCase
    @Inject
    internal lateinit var signInErrorData: SingleLiveEvent<String>
    @Inject
    internal lateinit var signInSuccessData: SingleLiveEvent<Void>
    @Inject
    internal lateinit var signInProgressData: SingleLiveEvent<Void>
    @Inject
    internal lateinit var invalidEmailData: SingleLiveEvent<String>
    @Inject
    internal lateinit var invalidPasswordData: SingleLiveEvent<String>

    init {
        appComponent.signIn()
                .inject(this)
    }

    fun signIn(email: String, password: String) {
        signInUseCase.signIn(email, password)
                .toLiveData(
                        onStart = { SignInStatus.Progress },
                        onSuccessMap = { it },
                        onFailureMap = { it }
                )
                .observeForever { status ->
                    when (status) {
                        is SignInStatus.Error -> signInErrorData.value = status.throwable.localizedMessage
                        is SignInStatus.Success -> signInSuccessData.call()
                        is SignInStatus.Progress -> signInProgressData.call()
                        is SignInStatus.InvalidEmail -> invalidEmailData.value = status.message
                        is SignInStatus.InvalidPassword -> invalidPasswordData.value = status.message
                    }
                }
    }

    fun signInError(): LiveData<String> = signInErrorData

    fun signInSuccess(): LiveData<Void> = signInSuccessData

    fun signInProgress(): LiveData<Void> = signInProgressData

    fun invalidEmail(): LiveData<String> = invalidEmailData

    fun invalidPassword(): LiveData<String> = invalidPasswordData
}
