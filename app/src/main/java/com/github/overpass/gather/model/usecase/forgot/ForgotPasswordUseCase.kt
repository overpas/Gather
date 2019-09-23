package com.github.overpass.gather.model.usecase.forgot

import com.github.overpass.gather.di.EMAIL_VALIDATOR
import com.github.overpass.gather.model.commons.exception.InvalidCredentialsException
import com.github.overpass.gather.model.data.validator.Validator
import com.github.overpass.gather.model.repo.password.PasswordResetRepo
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import javax.inject.Inject
import javax.inject.Named

class ForgotPasswordUseCase @Inject constructor(
        private val passwordResetRepo: PasswordResetRepo,
        @Named(EMAIL_VALIDATOR) private val validator: Validator<String>
) {

    fun sendForgotPassword(email: String): Task<Void> {
        return email.takeIf { validator.isValid(it) }
                ?.let { passwordResetRepo.sendForgotPassword(it) }
                ?: Tasks.forException(InvalidCredentialsException("Invalid Email"))
    }
}
