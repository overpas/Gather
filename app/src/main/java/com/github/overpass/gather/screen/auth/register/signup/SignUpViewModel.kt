package com.github.overpass.gather.screen.auth.register.signup

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.github.overpass.gather.model.commons.SingleLiveEvent
import com.github.overpass.gather.model.data.entity.splash.StartStatus
import com.github.overpass.gather.model.data.validator.EmailValidator
import com.github.overpass.gather.model.data.validator.PasswordValidator
import com.github.overpass.gather.model.repo.pref.PreferenceRepo
import com.github.overpass.gather.model.repo.register.SignUpRepo
import com.github.overpass.gather.model.usecase.login.StartStatusUseCase
import com.github.overpass.gather.model.usecase.register.SignUpUseCase
import com.github.overpass.gather.screen.auth.register.RegistrationStepViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : RegistrationStepViewModel(application) {

    private val signUpUseCase: SignUpUseCase = SignUpUseCase(
            SignUpRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()),
            EmailValidator(),
            PasswordValidator()
    )
    private val startStatusUseCase: StartStatusUseCase = StartStatusUseCase(
            PreferenceRepo(application.applicationContext),
            SignUpRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    )
    private val signUpErrorData = SingleLiveEvent<String>()
    private val signUpSuccessData = SingleLiveEvent<Void>()
    private val signUpProgressData = SingleLiveEvent<Void>()
    private val invalidEmailData = SingleLiveEvent<String>()
    private val invalidPasswordData = SingleLiveEvent<String>()

    @ExperimentalCoroutinesApi
    fun signUp(email: String, password: String) = viewModelScope.launch {
        signUpUseCase.signUp2(email, password)
                .collect {
                    when (it) {
                        is SignUpStatus.Error -> signUpErrorData.value = it.throwable.localizedMessage
                        is SignUpStatus.Success -> signUpSuccessData.call()
                        is SignUpStatus.Progress -> signUpProgressData.call()
                        is SignUpStatus.InvalidEmail -> invalidEmailData.value = it.message
                        is SignUpStatus.InvalidPassword -> invalidPasswordData.value = it.message
                    }
                }
    }

    fun setSignUpInProgress() {
        startStatusUseCase.setStartStatus(StartStatus.UNCONFIRMED_EMAIL)
    }

    fun signUpError(): LiveData<String> = signUpErrorData

    fun signUpSuccess(): LiveData<Void> = signUpSuccessData

    fun signUpProgress(): LiveData<Void> = signUpProgressData

    fun invalidEmail(): LiveData<String> = invalidEmailData

    fun invalidPassword(): LiveData<String> = invalidPasswordData
}
