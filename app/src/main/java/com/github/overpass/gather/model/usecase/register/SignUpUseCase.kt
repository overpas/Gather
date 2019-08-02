package com.github.overpass.gather.model.usecase.register

import com.github.overpass.gather.model.commons.mapToSuccess
import com.github.overpass.gather.model.data.validator.Validator
import com.github.overpass.gather.model.repo.register.SignUpRepo
import com.github.overpass.gather.screen.auth.register.signup.SignUpStatus
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

class SignUpUseCase(
        private val signUpRepo: SignUpRepo,
        private val emailValidator: Validator<String>,
        private val passwordValidator: Validator<String>
) {

    fun signUp(email: String, password: String): Task<SignUpStatus> {
        if (!emailValidator.isValid(email)) {
            return Tasks.forResult(SignUpStatus.InvalidEmail("Invalid Email"))
        }
        if (!passwordValidator.isValid(password)) {
            return Tasks.forResult(SignUpStatus.InvalidPassword("Invalid Password"))
        }
        return signUpRepo.signUp(email, password)
                .mapToSuccess(
                        successMapper = { SignUpStatus.Success },
                        failureMapper = { SignUpStatus.Error(it) }
                )
    }
}
