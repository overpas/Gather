package com.github.overpass.gather.screen.auth.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.github.overpass.gather.model.commons.SingleLiveEvent
import com.github.overpass.gather.model.commons.toLiveData
import com.github.overpass.gather.model.data.entity.signin.SignInStatus
import com.github.overpass.gather.model.data.validator.EmailValidator
import com.github.overpass.gather.model.data.validator.PasswordValidator
import com.github.overpass.gather.model.repo.login.AuthRepo
import com.github.overpass.gather.model.repo.pref.PreferenceRepo
import com.github.overpass.gather.model.usecase.login.SignInUseCase
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    private val signInUseCase: SignInUseCase = SignInUseCase(
            AuthRepo(FirebaseAuth.getInstance()),
            EmailValidator(),
            PasswordValidator(),
            PreferenceRepo(application)
    )
    private val signInErrorData: SingleLiveEvent<String> = SingleLiveEvent()
    private val signInSuccessData: SingleLiveEvent<Void> = SingleLiveEvent()
    private val signInProgressData: SingleLiveEvent<Void> = SingleLiveEvent()
    private val invalidEmailData: SingleLiveEvent<String> = SingleLiveEvent()
    private val invalidPasswordData: SingleLiveEvent<String> = SingleLiveEvent()

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
