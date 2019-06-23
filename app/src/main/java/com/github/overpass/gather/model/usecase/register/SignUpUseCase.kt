package com.github.overpass.gather.model.usecase.register

import androidx.lifecycle.LiveData

import com.github.overpass.gather.model.commons.LiveDataUtils
import com.github.overpass.gather.model.data.validator.BaseValidator
import com.github.overpass.gather.model.repo.register.SignUpRepo
import com.github.overpass.gather.screen.auth.register.signup.SignUpStatus

class SignUpUseCase(
        private val signUpRepo: SignUpRepo,
        private val validator: BaseValidator
) {

    fun signUp(email: String, password: String): LiveData<SignUpStatus> {
        if (!validator.isEmailValid(email)) {
            return LiveDataUtils.just(SignUpStatus.InvalidEmail("Invalid Email"))
        }
        if (!validator.isPasswordValid(password)) {
            return LiveDataUtils.just(SignUpStatus.InvalidPassword("Invalid Password"))
        }
        return signUpRepo.signUp(email, password)
    }
}
