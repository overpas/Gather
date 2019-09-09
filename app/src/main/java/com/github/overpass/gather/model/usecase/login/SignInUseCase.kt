package com.github.overpass.gather.model.usecase.login

import com.github.overpass.gather.di.EMAIL_VALIDATOR
import com.github.overpass.gather.di.PASSWORD_VALIDATOR
import com.github.overpass.gather.model.data.entity.signin.SignInStatus
import com.github.overpass.gather.model.data.entity.splash.StartStatus
import com.github.overpass.gather.model.data.validator.Validator
import com.github.overpass.gather.model.repo.login.AuthRepo
import com.github.overpass.gather.model.repo.pref.PreferenceRepo
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import javax.inject.Inject
import javax.inject.Named

class SignInUseCase @Inject constructor(
        private val authRepo: AuthRepo,
        @Named(EMAIL_VALIDATOR) private val emailValidator: Validator<String>,
        @Named(PASSWORD_VALIDATOR) private val passwordValidator: Validator<String>,
        private val preferenceRepo: PreferenceRepo
) {

    fun signIn(email: String, password: String): Task<SignInStatus> {
        if (!emailValidator.isValid(email)) {
            return Tasks.forResult(SignInStatus.InvalidEmail("Invalid Email"))
        }
        if (!passwordValidator.isValid(password)) {
            return Tasks.forResult(SignInStatus.InvalidPassword("Invalid Password"))
        }
        return authRepo.signIn(email, password)
                .onSuccessTask {
                    preferenceRepo.startStatus = StartStatus.AUTHORIZED
                    Tasks.forResult(it!!)
                }
    }
}
