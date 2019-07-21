package com.github.overpass.gather.model.repo.register

import com.github.overpass.gather.model.commons.Runners
import com.github.overpass.gather.model.commons.map
import com.github.overpass.gather.model.data.entity.user.User
import com.github.overpass.gather.model.repo.user.UsersData
import com.google.android.gms.tasks.SuccessContinuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignUpRepo(
        private val firebaseAuth: FirebaseAuth,
        private val firestore: FirebaseFirestore
) : UsersData {

    fun isUserNull(): Boolean = firebaseAuth.currentUser == null

    fun signUp(email: String, password: String): Task<Void> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
                .onSuccessTask<FirebaseUser>(Runners.io(), SuccessContinuation { authResult ->
                    val user = authResult!!.user
                    val email1 = authResult.user.email
                    firestore.collection(UsersData.COLLECTION_USERS)
                            .document(user.uid)
                            .set(User(email1, null, null))
                            .map { user }
                })
                .onSuccessTask(Runners.io(), SuccessContinuation {
                    it!!.sendEmailVerification()
                })
    }
}
