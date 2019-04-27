package com.github.overpass.gather.model.repo.password;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.auth.login.forgot.ForgotStatus;
import com.google.firebase.auth.FirebaseAuth;

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
