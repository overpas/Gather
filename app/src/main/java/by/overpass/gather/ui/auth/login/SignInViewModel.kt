package by.overpass.gather.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.overpass.gather.commons.android.lifecycle.JustLiveData
import by.overpass.gather.commons.android.lifecycle.SimpleLiveEvent
import by.overpass.gather.commons.android.lifecycle.trigger
import by.overpass.gather.commons.firebase.toLiveData
import by.overpass.gather.model.entity.signin.SignInStatus
import by.overpass.gather.model.usecase.login.SignInUseCase
import com.hadilq.liveevent.LiveEvent
import javax.inject.Inject

class SignInViewModel @Inject constructor(
        private val signInUseCase: SignInUseCase,
        private val signInErrorData: LiveEvent<String>,
        private val signInSuccessData: SimpleLiveEvent,
        private val signInProgressData: SimpleLiveEvent,
        private val invalidEmailData: LiveEvent<String>,
        private val invalidPasswordData: LiveEvent<String>
) : ViewModel() {

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
                        is SignInStatus.Success -> signInSuccessData.trigger()
                        is SignInStatus.Progress -> signInProgressData.trigger()
                        is SignInStatus.InvalidEmail -> invalidEmailData.value = status.message
                        is SignInStatus.InvalidPassword -> invalidPasswordData.value = status.message
                    }
                }
    }

    fun signInError(): LiveData<String> = signInErrorData

    fun signInSuccess(): JustLiveData = signInSuccessData

    fun signInProgress(): JustLiveData = signInProgressData

    fun invalidEmail(): LiveData<String> = invalidEmailData

    fun invalidPassword(): LiveData<String> = invalidPasswordData
}
