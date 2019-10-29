package by.overpass.gather.model.repo.password

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class PasswordResetRepo @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun sendForgotPassword(email: String): Task<Void> = firebaseAuth.sendPasswordResetEmail(email)
}
