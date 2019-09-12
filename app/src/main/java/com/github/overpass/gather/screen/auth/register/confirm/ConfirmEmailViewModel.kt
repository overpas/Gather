package com.github.overpass.gather.screen.auth.register.confirm

import android.app.Application
import androidx.lifecycle.LiveData
import com.github.overpass.gather.model.commons.SingleLiveEvent
import com.github.overpass.gather.model.commons.exception.NotAuthorized
import com.github.overpass.gather.model.commons.toLiveData
import com.github.overpass.gather.model.data.entity.confirm.ConfirmEmailStatus
import com.github.overpass.gather.model.data.entity.splash.StartStatus
import com.github.overpass.gather.model.usecase.confirm.ConfirmEmailUseCase
import com.github.overpass.gather.model.usecase.login.StartStatusUseCase
import com.github.overpass.gather.screen.auth.register.RegistrationStepViewModel
import javax.inject.Inject

class ConfirmEmailViewModel @Inject constructor(
        application: Application,
        private val confirmEmailUseCase: ConfirmEmailUseCase,
        private val startStatusUseCase: StartStatusUseCase,
        private val confirmErrorData: SingleLiveEvent<String>,
        private val confirmSuccessData: SingleLiveEvent<Void>,
        private val confirmProgressData: SingleLiveEvent<Void>
) : RegistrationStepViewModel(application) {

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
