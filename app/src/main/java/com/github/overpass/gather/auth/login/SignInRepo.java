package com.github.overpass.gather.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

class SignInRepo {

    LiveData<SignInStatus> signIn(String email, String password) {
        MutableLiveData<SignInStatus> signInStatus = new MutableLiveData<>();
        signInStatus.setValue(new SignInStatus.Progress());
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    signInStatus.setValue(new SignInStatus.Success());
                })
                .addOnFailureListener(e -> {
                    signInStatus.setValue(new SignInStatus.Error(e));
                });
        return signInStatus;
    }
}
