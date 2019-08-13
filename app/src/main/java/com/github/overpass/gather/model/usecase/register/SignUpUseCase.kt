package com.github.overpass.gather.model.usecase.register

import com.github.overpass.gather.model.commons.Result
import com.github.overpass.gather.model.data.validator.Validator
import com.github.overpass.gather.model.repo.register.SignUpRepo
import com.github.overpass.gather.screen.auth.register.signup.SignUpStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SignUpUseCase(
        private val signUpRepo: SignUpRepo,
        private val emailValidator: Validator<String>,
        private val passwordValidator: Validator<String>
) {

    @ExperimentalCoroutinesApi
    suspend fun signUp2(email: String, password: String): Flow<SignUpStatus> {
        return if (!emailValidator.isValid(email)) {
            flowOf(SignUpStatus.InvalidEmail("Invalid Email"))
        } else if (!passwordValidator.isValid(password)) {
            flowOf(SignUpStatus.InvalidPassword("Invalid Password"))
        } else {
            signUpRepo.signUp(email, password)
                    .map {
                        when (it) {
                            is Result.Loading -> SignUpStatus.Progress
                            is Result.Success -> SignUpStatus.Success
                            is Result.Error -> SignUpStatus.Error(it.exception)
                        }
                    }
        }
    }
}
