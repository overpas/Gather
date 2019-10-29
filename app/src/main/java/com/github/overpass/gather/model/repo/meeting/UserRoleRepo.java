package com.github.overpass.gather.model.repo.meeting;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.screen.map.AuthUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import javax.inject.Inject;

public class UserRoleRepo implements MeetingsMetadata {

    private final FirebaseFirestore firestore;

    @Inject
    public UserRoleRepo(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public LiveData<AuthUser.Role> getUserRole(String meetingId, String userId) {
        MutableLiveData<AuthUser.Role> roleData = new MutableLiveData<>();
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(Users.COLLECTION)
                .whereEqualTo(Users.FIELD_ID, userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    roleData.setValue(getRole(querySnapshot));
                })
                .addOnFailureListener(e -> {
                    roleData.setValue(AuthUser.Role.NOBODY);
                });
        return roleData;
    }

    private AuthUser.Role getRole(@Nullable QuerySnapshot querySnapshot) {
        AuthUser.Role result = AuthUser.Role.NOBODY;
        if (querySnapshot != null && !querySnapshot.isEmpty()) {
            Long role = querySnapshot.getDocuments().get(0).getLong(Users.FIELD_ROLE);
            if (role != null) {
                if (role == AuthUser.Role.ADMIN.getRole()) {
                    result = AuthUser.Role.ADMIN;
                } else if (role == AuthUser.Role.MODER.getRole()) {
                    result = AuthUser.Role.MODER;
                } else if (role == AuthUser.Role.USER.getRole()) {
                    result = AuthUser.Role.USER;
                }
            }
        }
        return result;
    }
}
