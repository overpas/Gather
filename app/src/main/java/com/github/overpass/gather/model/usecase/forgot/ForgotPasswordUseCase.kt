package com.github.overpass.gather.model.usecase.forgot

import com.github.overpass.gather.model.commons.exception.InvalidCredentialsException
import com.github.overpass.gather.model.data.validator.EmailValidator
import com.github.overpass.gather.model.data.validator.Validator
import com.github.overpass.gather.model.repo.password.PasswordResetRepo
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

class ForgotPasswordUseCase(
        private val passwordResetRepo: PasswordResetRepo,
        private val validator: Validator<String>
) {

    fun sendForgotPassword(email: String): Task<Void> {
        return email.takeIf { validator.isValid(it) }
                ?.let { passwordResetRepo.sendForgotPassword(it) }
                ?: Tasks.forException(InvalidCredentialsException("Invalid Email"))
    }
}
