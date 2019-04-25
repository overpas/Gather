package com.github.overpass.gather.auth.register.add;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

class UserRepo {

    private final FirebaseAuth firebaseAuth;

    UserRepo(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    LiveData<SaveUserStatus> save(String username, @Nullable Uri imageUri) {
        MutableLiveData<SaveUserStatus> saveUserStatus = new MutableLiveData<>();
        saveUserStatus.setValue(new SaveUserStatus.Progress());
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .setPhotoUri(imageUri)
                    .build();
            currentUser.updateProfile(profileChangeRequest)
                    .addOnSuccessListener(result -> {
                        saveUserStatus.setValue(new SaveUserStatus.Success());
                    })
                    .addOnFailureListener(e -> {
                        saveUserStatus.setValue(new SaveUserStatus.Error(e));
                    });
        } else {
            saveUserStatus.setValue(new SaveUserStatus.Error(new Throwable("User not found")));
        }
        return saveUserStatus;
    }
}
