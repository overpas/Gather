package com.github.overpass.gather.model.usecase.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

import com.github.overpass.gather.model.commons.LiveDataUtils
import com.github.overpass.gather.model.commons.then
import com.github.overpass.gather.model.data.validator.BaseValidator
import com.github.overpass.gather.model.repo.pref.PreferenceRepo
import com.github.overpass.gather.screen.auth.login.SignInStatus
import com.github.overpass.gather.model.repo.login.AuthRepo
import com.github.overpass.gather.screen.splash.StartStatus

class SignInUseCase(
        private val authRepo: AuthRepo,
        private val validator: BaseValidator,
        private val preferenceRepo: PreferenceRepo
) {

    fun signIn(email: String, password: String): LiveData<SignInStatus> {
        if (!validator.isEmailValid(email)) {
            return LiveDataUtils.just(SignInStatus.InvalidEmail("Invalid Email"))
        }
        if (!validator.isPasswordValid(password)) {
            return LiveDataUtils.just(SignInStatus.InvalidPassword("Invalid Password"))
        }
        return authRepo.signIn(email, password)
                .then { preferenceRepo.startStatus = StartStatus.AUTHORIZED }
    }
}
