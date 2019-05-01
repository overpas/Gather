package com.github.overpass.gather.model.repo.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.model.repo.user.UsersData;
import com.github.overpass.gather.screen.auth.register.signup.SignUpStatus;
import com.github.overpass.gather.screen.auth.register.signup.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpRepo implements UsersData {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    public SignUpRepo(FirebaseAuth firebaseAuth, FirebaseFirestore firestore) {
        this.firebaseAuth = firebaseAuth;
        this.firestore = firestore;
    }

    public LiveData<SignUpStatus> signUp(String email, String password) {
        MutableLiveData<SignUpStatus> signUpStatus = new MutableLiveData<>();
        signUpStatus.setValue(new SignUpStatus.Progress());
        final FirebaseUser[] user = new FirebaseUser[1];
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(e -> {
                    signUpStatus.setValue(new SignUpStatus.Error(e));
                })
                .onSuccessTask(authResult -> {
                    user[0] = authResult.getUser();
                    String email1 = authResult.getUser().getEmail();
                    return firestore.collection(COLLECTION_USERS)
                            .document(user[0].getUid())
                            .set(new User(email1, null, null));
                })
                .onSuccessTask(docRef -> user[0].sendEmailVerification())
                .addOnSuccessListener(__ -> {
                    signUpStatus.setValue(new SignUpStatus.Success());
                })
                .addOnFailureListener(e -> {
                    signUpStatus.setValue(new SignUpStatus.Error(e));
                });
        return signUpStatus;
    }
}
