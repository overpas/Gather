package com.github.overpass.gather.model.repo.subscription;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.messaging.FirebaseMessaging;

public class SubscriptionRepo {

    private static final String POSTFIX_PENDING_USERS = "_pending_users";

    private final FirebaseMessaging messaging;

    public SubscriptionRepo(FirebaseMessaging messaging) {
        this.messaging = messaging;
    }

    public LiveData<Boolean> subscribeToPendingUsers(String meetingId) {
        MutableLiveData<Boolean> subData = new MutableLiveData<>();
        messaging.subscribeToTopic(meetingId + POSTFIX_PENDING_USERS)
                .addOnSuccessListener((v) -> {
                    subData.setValue(true);
                })
                .addOnFailureListener(e -> {
                    subData.setValue(false);
                });
        return subData;
    }
}
