package com.github.overpass.gather.model.repo.login

import androidx.lifecycle.LiveData
import com.github.overpass.gather.model.commons.toLiveData
import com.github.overpass.gather.model.data.entity.signin.SignInStatus
import com.google.firebase.auth.FirebaseAuth

class AuthRepo(private val auth: FirebaseAuth) {

    fun signIn(email: String, password: String): LiveData<SignInStatus> {
        return auth.signInWithEmailAndPassword(email, password)
                .toLiveData(
                        { SignInStatus.Progress },
                        { SignInStatus.Success },
                        { SignInStatus.Error(it) }
                )
    }

    fun signOut(onSuccess: Runnable) {
        auth.signOut()
        onSuccess.run()
    }
}
