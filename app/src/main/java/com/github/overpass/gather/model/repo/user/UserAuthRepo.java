package com.github.overpass.gather.model.repo.user;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.screen.auth.register.add.SaveUserStatus;
import com.github.overpass.gather.screen.map.AuthUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URL;

public class UserAuthRepo implements UsersData {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    public UserAuthRepo(FirebaseAuth firebaseAuth, FirebaseFirestore firestore) {
        this.firebaseAuth = firebaseAuth;
        this.firestore = firestore;
    }

    public LiveData<SaveUserStatus> save(String username, @Nullable Uri imageUri) {
        MutableLiveData<SaveUserStatus> saveUserStatus = new MutableLiveData<>();
        saveUserStatus.setValue(new SaveUserStatus.Progress());
        if (firebaseAuth.getCurrentUser() != null) {
            saveUserStatus = saveWithCurrentUser(firebaseAuth.getCurrentUser(), username, imageUri);
        } else {
            saveUserStatus.setValue(new SaveUserStatus.Error(new Throwable("User not found")));
        }
        return saveUserStatus;
    }

    private MutableLiveData<SaveUserStatus> saveWithCurrentUser(@NonNull FirebaseUser user,
                                                                String username,
                                                                @Nullable Uri imageUri) {
        MutableLiveData<SaveUserStatus> saveUserStatus = new MutableLiveData<>();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(imageUri)
                .build();
        user.updateProfile(profileChangeRequest)
                .onSuccessTask(__ -> firestore.collection(COLLECTION_USERS)
                        .document(user.getUid())
                        .update(FIELD_USERNAME, username, FIELD_PHOTO_URL,
                                imageUri == null ? null : imageUri.toString()))
                .addOnSuccessListener(result -> {
                    saveUserStatus.setValue(new SaveUserStatus.Success());
                })
                .addOnFailureListener(e -> {
                    saveUserStatus.setValue(new SaveUserStatus.Error(e));
                });
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
