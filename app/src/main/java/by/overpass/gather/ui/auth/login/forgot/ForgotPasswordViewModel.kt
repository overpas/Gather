package by.overpass.gather.ui.auth.login.forgot

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.overpass.gather.commons.android.lifecycle.JustLiveData
import by.overpass.gather.commons.android.lifecycle.SimpleLiveEvent
import by.overpass.gather.commons.android.lifecycle.trigger
import by.overpass.gather.commons.exception.InvalidCredentialsException
import by.overpass.gather.commons.firebase.toLiveData
import by.overpass.gather.model.entity.forgot.ForgotStatus
import by.overpass.gather.model.usecase.forgot.ForgotPasswordUseCase
import com.hadilq.liveevent.LiveEvent
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(
        private val forgotPasswordUseCase: ForgotPasswordUseCase,
        private val resetPasswordErrorData: LiveEvent<String>,
        private val resetPasswordSuccessData: SimpleLiveEvent,
        private val invalidEmailData: SimpleLiveEvent,
        private val resetPasswordProgressData: SimpleLiveEvent
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
                        is ForgotStatus.Success -> resetPasswordSuccessData.trigger()
                        is ForgotStatus.InvalidEmail -> invalidEmailData.trigger()
                        is ForgotStatus.Progress -> resetPasswordProgressData.trigger()
                    }
                }
    }

    private fun checkReason(exception: Exception): ForgotStatus {
        exception.takeIf { it is InvalidCredentialsException }
                ?.let { return ForgotStatus.InvalidEmail }
                ?: return ForgotStatus.Error(exception)
    }

    fun resetPasswordError(): LiveData<String> = resetPasswordErrorData
    fun resetPasswordSuccess(): JustLiveData = resetPasswordSuccessData
    fun invalidEmail(): JustLiveData = invalidEmailData
    fun resetPasswordProgress(): JustLiveData = resetPasswordProgressData
}
