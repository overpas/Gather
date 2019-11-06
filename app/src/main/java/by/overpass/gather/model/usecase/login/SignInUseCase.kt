package by.overpass.gather.model.usecase.login

import by.overpass.gather.di.EMAIL_VALIDATOR
import by.overpass.gather.di.PASSWORD_VALIDATOR
import by.overpass.gather.model.entity.signin.SignInStatus
import by.overpass.gather.model.entity.splash.StartStatus
import by.overpass.gather.model.validator.Validator
import by.overpass.gather.data.repo.login.AuthRepo
import by.overpass.gather.data.repo.pref.PreferenceRepo
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
