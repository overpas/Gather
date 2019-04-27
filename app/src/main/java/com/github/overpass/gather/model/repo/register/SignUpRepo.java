package com.github.overpass.gather.model.repo.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.auth.register.signup.SignUpStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpRepo {

    private final FirebaseAuth firebaseAuth;

    public SignUpRepo(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    private LiveData<SignUpStatus> sendConfirmationCode() {
        MutableLiveData<SignUpStatus> signUpStatus = new MutableLiveData<>();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null && !currentUser.isEmailVerified()) {
            currentUser.sendEmailVerification()
                    .addOnSuccessListener((result) -> {
                        signUpStatus.setValue(new SignUpStatus.Success());
                    })
                    .addOnFailureListener((e) -> {
                        signUpStatus.setValue(new SignUpStatus.Error(e));
                    });
        }
        return signUpStatus;
    }

    public LiveData<SignUpStatus> signUp(String email, String password) {
        MutableLiveData<SignUpStatus> signUpStatus = new MutableLiveData<>();
        signUpStatus.setValue(new SignUpStatus.Progress());
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    sendConfirmationCode().observeForever(signUpStatus::setValue);
                })
                .addOnFailureListener(e -> {
                    signUpStatus.setValue(new SignUpStatus.Error(e));
                });
        return signUpStatus;
    }
}
