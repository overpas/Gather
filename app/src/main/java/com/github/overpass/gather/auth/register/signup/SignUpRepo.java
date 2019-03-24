package com.github.overpass.gather.auth.register.signup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

class SignUpRepo {

    private final FirebaseAuth firebaseAuth;

    SignUpRepo(FirebaseAuth firebaseAuth) {
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

    LiveData<SignUpStatus> signUp(String email, String password) {
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
