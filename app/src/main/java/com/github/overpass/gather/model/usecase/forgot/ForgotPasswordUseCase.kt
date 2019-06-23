package com.github.overpass.gather.model.usecase.forgot

import androidx.lifecycle.LiveData

import com.github.overpass.gather.model.commons.LiveDataUtils
import com.github.overpass.gather.model.data.validator.BaseValidator
import com.github.overpass.gather.model.repo.password.PasswordResetRepo
import com.github.overpass.gather.model.data.entity.forgot.ForgotStatus

class ForgotPasswordUseCase(
        private val passwordResetRepo: PasswordResetRepo,
        private val validator: BaseValidator
) {

    fun sendForgotPassword(email: String): LiveData<out ForgotStatus> {
        return email.takeIf { validator.isEmailValid(it) }
                ?.let { passwordResetRepo.sendForgotPassword(it) }
                ?: LiveDataUtils.just(ForgotStatus.InvalidEmail)
    }
}
