package by.overpass.gather.ui.auth.register.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.overpass.gather.commons.android.lifecycle.JustLiveData
import by.overpass.gather.commons.android.lifecycle.SimpleLiveEvent
import by.overpass.gather.commons.android.lifecycle.trigger
import by.overpass.gather.commons.exception.NotAuthorized
import by.overpass.gather.commons.firebase.toLiveData
import by.overpass.gather.model.entity.confirm.ConfirmEmailStatus
import by.overpass.gather.model.entity.splash.StartStatus
import by.overpass.gather.model.usecase.confirm.ConfirmEmailUseCase
import by.overpass.gather.model.usecase.login.StartStatusUseCase
import com.hadilq.liveevent.LiveEvent
import javax.inject.Inject

class ConfirmEmailViewModel @Inject constructor(
        private val confirmEmailUseCase: ConfirmEmailUseCase,
        private val startStatusUseCase: StartStatusUseCase,
        private val confirmErrorData: LiveEvent<String>,
        private val confirmSuccessData: SimpleLiveEvent,
        private val confirmProgressData: SimpleLiveEvent
) : ViewModel() {

    fun confirm() {
        confirmEmailUseCase.confirmEmail()
                .toLiveData(
                        onSuccessMap = { checkEmail() },
                        onFailureMap = { ConfirmEmailStatus.Error(it) }
                )
                .observeForever {
                    when (it) {
                        is ConfirmEmailStatus.Success -> confirmSuccessData.trigger()
                        is ConfirmEmailStatus.Error -> confirmErrorData.value = it.throwable.localizedMessage
                        is ConfirmEmailStatus.Progress -> confirmProgressData.trigger()
                    }
                }
    }

    private fun checkEmail(): ConfirmEmailStatus = if (confirmEmailUseCase.isEmailVerified()) {
        ConfirmEmailStatus.Success
    } else {
        ConfirmEmailStatus.Error(NotAuthorized())
    }

    fun setEmailConfirmed() {
        startStatusUseCase.setStartStatus(StartStatus.NOT_ADDED_DATA)
    }

    fun confirmationError(): LiveData<String> = confirmErrorData
    fun confirmationSuccess(): JustLiveData = confirmSuccessData
    fun confirmationProgress(): JustLiveData = confirmProgressData
}
