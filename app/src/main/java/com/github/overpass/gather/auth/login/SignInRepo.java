package com.github.overpass.gather.auth.login;

import com.github.overpass.gather.SingleLiveEvent;
import com.google.firebase.auth.FirebaseAuth;

public class SignInRepo {

    public void signIn(SingleLiveEvent<SignInStatus> signInStatus, String email, String password) {
        signInStatus.setValue(new SignInStatus.Progress());
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    signInStatus.setValue(new SignInStatus.Success());
                })
                .addOnFailureListener(e -> {
                    signInStatus.setValue(new SignInStatus.Error(e));
                });
    }
}
