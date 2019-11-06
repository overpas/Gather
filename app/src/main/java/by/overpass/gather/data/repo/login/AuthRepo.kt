package by.overpass.gather.data.repo.login

import by.overpass.gather.commons.firebase.mapToSuccess
import by.overpass.gather.model.entity.signin.SignInStatus
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepo @Inject constructor(private val auth: FirebaseAuth) {

    fun signIn(email: String, password: String): Task<SignInStatus> {
        return auth.signInWithEmailAndPassword(email, password)
                .mapToSuccess(
                        successMapper = { SignInStatus.Success },
                        failureMapper = { SignInStatus.Error(it) }
                )
    }

    fun signOut() {
        auth.signOut()
    }
}
