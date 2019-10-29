package com.github.overpass.gather.model.repo.register

import com.github.overpass.gather.di.IO_DISPATCHER
import com.github.overpass.gather.commons.abstractions.Result
import com.github.overpass.gather.commons.firebase.asResultFlow
import com.github.overpass.gather.commons.firebase.map
import com.github.overpass.gather.model.data.entity.user.User
import com.github.overpass.gather.model.repo.user.UsersData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class SignUpRepo @Inject constructor(
        private val firebaseAuth: FirebaseAuth,
        private val firestore: FirebaseFirestore,
        @Named(IO_DISPATCHER) private val ioDispatcher: CoroutineDispatcher
) {

    fun isUserNull(): Boolean = firebaseAuth.currentUser == null

    @ExperimentalCoroutinesApi
    suspend fun signUp(
            email: String,
            password: String
    ): Flow<Result<Unit>> = withContext(ioDispatcher) {
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
