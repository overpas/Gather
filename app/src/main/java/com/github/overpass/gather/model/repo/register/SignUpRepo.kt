package com.github.overpass.gather.model.repo.register

import com.github.overpass.gather.model.commons.Result
import com.github.overpass.gather.model.commons.asResultFlow
import com.github.overpass.gather.model.commons.map
import com.github.overpass.gather.model.data.entity.user.User
import com.github.overpass.gather.model.repo.user.UsersData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SignUpRepo(
        private val firebaseAuth: FirebaseAuth,
        private val firestore: FirebaseFirestore
) : UsersData {

    fun isUserNull(): Boolean = firebaseAuth.currentUser == null

    @ExperimentalCoroutinesApi
    suspend fun signUp(
            email: String,
            password: String
    ): Flow<Result<Unit>> = withContext(Dispatchers.IO) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .onSuccessTask<FirebaseUser> { authResult ->
                    val user = authResult!!.user
                    val email1 = authResult.user!!.email
                    firestore.collection(UsersData.COLLECTION_USERS)
                            .document(user!!.uid)
                            .set(User(email1, null, null))
                            .map { user }
                }
                .onSuccessTask<Void> {
                    it!!.sendEmailVerification()
                }
                .asResultFlow()
    }
}
