package com.github.overpass.gather.auth.register;

import com.github.overpass.gather.SingleLiveEvent;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpRepo {

    public void signUp(SingleLiveEvent<SignUpStatus> signUpData, String email, String password) {
        signUpData.setValue(new SignUpStatus.Progress());
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    signUpData.setValue(new SignUpStatus.Success());
                })
                .addOnFailureListener(e -> {
                    signUpData.setValue(new SignUpStatus.Error(e));
                });
    }
}
