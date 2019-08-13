package com.github.overpass.gather.model.repo.confirm

import com.github.overpass.gather.model.commons.exception.NotAuthorized
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth

class ConfirmEmailRepo(private val firebaseAuth: FirebaseAuth) {

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
