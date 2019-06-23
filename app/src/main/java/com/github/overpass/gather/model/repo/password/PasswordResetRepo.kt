package com.github.overpass.gather.model.repo.password

import androidx.lifecycle.LiveData
import com.github.overpass.gather.model.commons.toLiveData

import com.github.overpass.gather.model.data.entity.forgot.ForgotStatus
import com.google.firebase.auth.FirebaseAuth

class PasswordResetRepo(private val firebaseAuth: FirebaseAuth) {

    fun sendForgotPassword(email: String): LiveData<ForgotStatus> {
        return firebaseAuth.sendPasswordResetEmail(email)
                .toLiveData(
                        onStart = { ForgotStatus.Progress },
                        onSuccessMap = { ForgotStatus.Success },
                        onFailureMap = { ForgotStatus.Error(it) }
                )
    }
}
