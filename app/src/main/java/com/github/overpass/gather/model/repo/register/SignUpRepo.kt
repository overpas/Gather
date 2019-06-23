package com.github.overpass.gather.model.repo.register

import androidx.lifecycle.LiveData
import com.github.overpass.gather.model.commons.Runners
import com.github.overpass.gather.model.commons.map
import com.github.overpass.gather.model.commons.toLiveData
import com.github.overpass.gather.model.repo.user.UsersData
import com.github.overpass.gather.screen.auth.register.signup.SignUpStatus
import com.github.overpass.gather.model.data.entity.user.User
import com.google.android.gms.tasks.SuccessContinuation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignUpRepo(
        private val firebaseAuth: FirebaseAuth,
        private val firestore: FirebaseFirestore
) : UsersData {

    fun isUserNull(): Boolean = firebaseAuth.currentUser == null

    fun signUp(email: String, password: String): LiveData<SignUpStatus> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
                .onSuccessTask<FirebaseUser>(Runners.io(), SuccessContinuation { authResult ->
                    val user = authResult!!.user
                    val email1 = authResult.user.email
                    firestore.collection(UsersData.COLLECTION_USERS)
                            .document(user.uid)
                            .set(User(email1, null, null))
                            .map { user }
                })
                .onSuccessTask<Void>(Runners.io(), SuccessContinuation {
                    it!!.sendEmailVerification()
                })
                .toLiveData(
                        onStart = { SignUpStatus.Progress },
                        onSuccessMap = { SignUpStatus.Success },
                        onFailureMap = { SignUpStatus.Error(it) }
                )
    }
}
