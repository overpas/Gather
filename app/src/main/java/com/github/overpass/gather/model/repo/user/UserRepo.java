package com.github.overpass.gather.model.repo.user;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.screen.auth.register.add.SaveUserStatus;
import com.github.overpass.gather.screen.map.AuthUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserRepo {

    private final FirebaseAuth firebaseAuth;

    public UserRepo(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<SaveUserStatus> save(String username, @Nullable Uri imageUri) {
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

    // null if not authorized
    public LiveData<AuthUser> getCurrentUser(AuthUser.Role role) {
        MutableLiveData<AuthUser> authUserData = new MutableLiveData<>();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            authUserData.setValue(null);
        } else {
            authUserData.setValue(new AuthUser(role.getRole(), currentUser.getUid()));
        }
        return authUserData;
    }
}
