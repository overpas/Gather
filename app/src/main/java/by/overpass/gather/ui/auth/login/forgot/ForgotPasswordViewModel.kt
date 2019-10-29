package by.overpass.gather.ui.auth.login.forgot

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import by.overpass.gather.commons.exception.InvalidCredentialsException
import by.overpass.gather.commons.firebase.toLiveData
import by.overpass.gather.model.data.entity.forgot.ForgotStatus
import by.overpass.gather.model.usecase.forgot.ForgotPasswordUseCase
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(
        private val forgotPasswordUseCase: ForgotPasswordUseCase,
        private val resetPasswordErrorData: SingleLiveEvent<String>,
        private val resetPasswordSuccessData: SingleLiveEvent<Void>,
        private val invalidEmailData: SingleLiveEvent<Void>,
        private val resetPasswordProgressData: SingleLiveEvent<Void>
) : ViewModel() {

    fun sendForgotPassword(email: String) {
        forgotPasswordUseCase.sendForgotPassword(email)
                .toLiveData(
                        onStart = { ForgotStatus.Progress },
                        onSuccessMap = { ForgotStatus.Success },
                        onFailureMap = { checkReason(it) }
                )
                .observeForever {
                    when (it) {
                        is ForgotStatus.Error -> resetPasswordErrorData.value = it.throwable.localizedMessage
                        is ForgotStatus.Success -> resetPasswordSuccessData.call()
                        is ForgotStatus.InvalidEmail -> invalidEmailData.call()
                        is ForgotStatus.Progress -> resetPasswordProgressData.call()
                    }
                }
    }

    private fun checkReason(exception: Exception): ForgotStatus {
        exception.takeIf { it is InvalidCredentialsException }
                ?.let { return ForgotStatus.InvalidEmail }
                ?: return ForgotStatus.Error(exception)
    }

    fun resetPasswordError(): LiveData<String> = resetPasswordErrorData
    fun resetPasswordSuccess(): LiveData<Void> = resetPasswordSuccessData
    fun invalidEmail(): LiveData<Void> = invalidEmailData
    fun resetPasswordProgress(): LiveData<Void> = resetPasswordProgressData
}
