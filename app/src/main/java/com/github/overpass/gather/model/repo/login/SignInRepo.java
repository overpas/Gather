package com.github.overpass.gather.model.repo.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.screen.auth.login.SignInStatus;
import com.google.firebase.auth.FirebaseAuth;

public class SignInRepo {

    public LiveData<SignInStatus> signIn(String email, String password) {
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
