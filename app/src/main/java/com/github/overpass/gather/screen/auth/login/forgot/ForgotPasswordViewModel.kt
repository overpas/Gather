package com.github.overpass.gather.screen.auth.login.forgot

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.overpass.gather.model.commons.SingleLiveEvent
import com.github.overpass.gather.model.data.entity.forgot.ForgotStatus
import com.github.overpass.gather.model.data.validator.BaseValidator
import com.github.overpass.gather.model.repo.password.PasswordResetRepo
import com.github.overpass.gather.model.usecase.forgot.ForgotPasswordUseCase
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {

    private val forgotPasswordUseCase: ForgotPasswordUseCase = ForgotPasswordUseCase(
            PasswordResetRepo(FirebaseAuth.getInstance()),
            BaseValidator()
    )
    private val resetPasswordErrorData = SingleLiveEvent<String>()
    private val resetPasswordSuccessData = SingleLiveEvent<Void>()
    private val invalidEmailData = SingleLiveEvent<Void>()
    private val resetPasswordProgressData = SingleLiveEvent<Void>()

    fun sendForgotPassword(email: String) {
        forgotPasswordUseCase.sendForgotPassword(email).observeForever {
            when (it) {
                is ForgotStatus.Error -> resetPasswordErrorData.value = it.throwable.localizedMessage
                is ForgotStatus.Success -> resetPasswordSuccessData.call()
                is ForgotStatus.InvalidEmail -> invalidEmailData.call()
                is ForgotStatus.Progress -> resetPasswordProgressData.call()
            }
        }
    }

    fun resetPasswordError(): LiveData<String> = resetPasswordErrorData
    fun resetPasswordSuccess(): LiveData<Void> = resetPasswordSuccessData
    fun invalidEmail(): LiveData<Void> = invalidEmailData
    fun resetPasswordProgress(): LiveData<Void> = resetPasswordProgressData
}
