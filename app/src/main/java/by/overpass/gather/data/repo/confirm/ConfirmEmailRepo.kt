package by.overpass.gather.data.repo.confirm

import by.overpass.gather.commons.exception.NotAuthorized
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class ConfirmEmailRepo @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun isEmailVerified(): Boolean {
        return firebaseAuth.currentUser
                ?.isEmailVerified
                ?: false
    }

    fun confirmEmail(): Task<Void> {
        return firebaseAuth.currentUser
                ?.reload()
                ?: Tasks.forException(NotAuthorized())
    }
}
