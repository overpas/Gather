package com.github.overpass.gather.model.repo.confirm

import androidx.lifecycle.LiveData
import com.github.overpass.gather.model.commons.LiveDataUtils
import com.github.overpass.gather.model.commons.toLiveData

import com.github.overpass.gather.model.data.entity.confirm.ConfirmEmailStatus
import com.google.firebase.auth.FirebaseAuth

class ConfirmEmailRepo(private val firebaseAuth: FirebaseAuth) {

    private fun isEmailVerified(): ConfirmEmailStatus {
        val user = firebaseAuth.currentUser
        return if (user != null && user.isEmailVerified) {
            ConfirmEmailStatus.Success
        } else {
            val throwable = Throwable("Sorry, your email hasn't been verified")
            ConfirmEmailStatus.Error(throwable)
        }
    }

    fun confirmEmail(): LiveData<out ConfirmEmailStatus> {
        return firebaseAuth.currentUser
                ?.reload()
                ?.toLiveData(
                        onSuccessMap = { isEmailVerified() },
                        onFailureMap = { ConfirmEmailStatus.Error(it) }
                ) ?: LiveDataUtils.just(ConfirmEmailStatus.Error(Throwable("Something went wrong")))
    }
}
