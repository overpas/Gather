package by.overpass.gather.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import by.overpass.gather.commons.firebase.toLiveData
import by.overpass.gather.model.data.entity.signin.SignInStatus
import by.overpass.gather.model.usecase.login.SignInUseCase
import javax.inject.Inject

class SignInViewModel @Inject constructor(
        private val signInUseCase: SignInUseCase,
        private val signInErrorData: SingleLiveEvent<String>,
        private val signInSuccessData: SingleLiveEvent<Void>,
        private val signInProgressData: SingleLiveEvent<Void>,
        private val invalidEmailData: SingleLiveEvent<String>,
        private val invalidPasswordData: SingleLiveEvent<String>
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
