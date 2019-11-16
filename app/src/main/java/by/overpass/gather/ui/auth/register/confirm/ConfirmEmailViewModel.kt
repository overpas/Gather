package by.overpass.gather.ui.auth.register.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import by.overpass.gather.commons.exception.NotAuthorized
import by.overpass.gather.commons.firebase.toLiveData
import by.overpass.gather.model.entity.confirm.ConfirmEmailStatus
import by.overpass.gather.model.entity.splash.StartStatus
import by.overpass.gather.model.usecase.confirm.ConfirmEmailUseCase
import by.overpass.gather.model.usecase.login.StartStatusUseCase
import javax.inject.Inject

class ConfirmEmailViewModel @Inject constructor(
        private val confirmEmailUseCase: ConfirmEmailUseCase,
        private val startStatusUseCase: StartStatusUseCase,
        private val confirmErrorData: SingleLiveEvent<String>,
        private val confirmSuccessData: SingleLiveEvent<Void>,
        private val confirmProgressData: SingleLiveEvent<Void>
) : ViewModel() {

    fun confirm() {
        confirmEmailUseCase.confirmEmail()
                .toLiveData(
                        onSuccessMap = { checkEmail() },
                        onFailureMap = { ConfirmEmailStatus.Error(it) }
                )
                .observeForever {
                    when (it) {
                        is ConfirmEmailStatus.Success -> confirmSuccessData.call()
                        is ConfirmEmailStatus.Error -> confirmErrorData.value = it.throwable.localizedMessage
                        is ConfirmEmailStatus.Progress -> confirmProgressData.call()
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
    fun confirmationSuccess(): LiveData<Void> = confirmSuccessData
    fun confirmationProgress(): LiveData<Void> = confirmProgressData
}
