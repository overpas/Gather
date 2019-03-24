package com.github.overpass.gather.auth.register.signup;

import com.github.overpass.gather.SingleLiveEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executors;

class SignUpRepo {

    private final FirebaseAuth firebaseAuth;

    SignUpRepo(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    void signUp(SingleLiveEvent<SignUpStatus> signUpData, String email, String password) {
        signUpData.setValue(new SignUpStatus.Progress());
        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(Executors.newSingleThreadExecutor(), authResult -> {
                    sendConfirmationCode(signUpData);
                })
                .addOnFailureListener(e -> {
                    signUpData.setValue(new SignUpStatus.Error(e));
                });
    }

    void sendConfirmationCode(SingleLiveEvent<SignUpStatus> signUpData) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null && !currentUser.isEmailVerified()) {
            currentUser.sendEmailVerification()
                    .addOnSuccessListener((result) -> {
                        signUpData.setValue(new SignUpStatus.Success());
                    })
                    .addOnFailureListener((e) -> {
                        signUpData.setValue(new SignUpStatus.Error(e));
                    });
        }
    }
}
