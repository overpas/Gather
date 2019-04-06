package com.github.overpass.gather.auth.login.forgot;

import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PasswordResetRepo {

    private final FirebaseAuth firebaseAuth;

    public PasswordResetRepo(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<ForgotStatus> sendForgotPassword(String email) {
        MutableLiveData<ForgotStatus> result = new MutableLiveData<>();
        result.setValue(new ForgotStatus.Progress());
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(__ -> {
                    result.setValue(new ForgotStatus.Success());
                })
                .addOnFailureListener(e -> {
                    result.setValue(new ForgotStatus.Error(e));
                });
        return result;
    }
}
