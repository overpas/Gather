package by.overpass.gather.ui.auth.register.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.overpass.gather.commons.android.lifecycle.JustLiveData
import by.overpass.gather.commons.android.lifecycle.SimpleLiveEvent
import by.overpass.gather.commons.android.lifecycle.trigger
import by.overpass.gather.model.entity.splash.StartStatus
import by.overpass.gather.model.usecase.login.StartStatusUseCase
import by.overpass.gather.model.usecase.register.SignUpUseCase
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
        private val signUpUseCase: SignUpUseCase,
        private val startStatusUseCase: StartStatusUseCase,
        private val signUpErrorData: LiveEvent<String>,
        private val signUpSuccessData: SimpleLiveEvent,
        private val signUpProgressData: SimpleLiveEvent,
        private val invalidEmailData: LiveEvent<String>,
        private val invalidPasswordData: LiveEvent<String>
) : ViewModel() {

    @ExperimentalCoroutinesApi
    fun signUp(email: String, password: String) = viewModelScope.launch {
        signUpUseCase.signUp2(email, password)
                .collect {
                    when (it) {
                        is SignUpStatus.Error -> signUpErrorData.value = it.throwable.localizedMessage
                        is SignUpStatus.Success -> signUpSuccessData.trigger()
                        is SignUpStatus.Progress -> signUpProgressData.trigger()
                        is SignUpStatus.InvalidEmail -> invalidEmailData.value = it.message
                        is SignUpStatus.InvalidPassword -> invalidPasswordData.value = it.message
                    }
                }
    }

    fun setSignUpInProgress() {
        startStatusUseCase.setStartStatus(StartStatus.UNCONFIRMED_EMAIL)
    }

    fun signUpError(): LiveData<String> = signUpErrorData

    fun signUpSuccess(): JustLiveData = signUpSuccessData

    fun signUpProgress(): JustLiveData = signUpProgressData

    fun invalidEmail(): LiveData<String> = invalidEmailData

    fun invalidPassword(): LiveData<String> = invalidPasswordData
}
