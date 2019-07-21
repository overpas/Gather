package com.github.overpass.gather.model.repo.password

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class PasswordResetRepo(private val firebaseAuth: FirebaseAuth) {

    fun sendForgotPassword(email: String): Task<Void> = firebaseAuth.sendPasswordResetEmail(email)
}
