package com.github.overpass.gather.model.usecase.login

import com.github.overpass.gather.model.data.entity.signin.SignInStatus
import com.github.overpass.gather.model.data.entity.splash.StartStatus
import com.github.overpass.gather.model.data.validator.EmailValidator
import com.github.overpass.gather.model.data.validator.PasswordValidator
import com.github.overpass.gather.model.repo.login.AuthRepo
import com.github.overpass.gather.model.repo.pref.PreferenceRepo
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import javax.inject.Inject

class SignInUseCase @Inject constructor(
        private val authRepo: AuthRepo,
        private val emailValidator: EmailValidator,
        private val passwordValidator: PasswordValidator,
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
