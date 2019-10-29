package com.github.overpass.gather.model.repo.subscription;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.ui.map.AuthUser;
import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;

public class SubscriptionRepo {

    private static final String POSTFIX_PENDING_USERS = "_pending_users";
    private static final String INTERFIX_ACCEPTED = "_accepted_";

    private final FirebaseMessaging messaging;

    @Inject
    public SubscriptionRepo(FirebaseMessaging messaging) {
        this.messaging = messaging;
    }

    public LiveData<Boolean> subscribeToPendingUsers(String meetingId) {
        return subscribeToTopic(meetingId + POSTFIX_PENDING_USERS);
    }

    public LiveData<Boolean> subscribeToAccepted(AuthUser authUser, String meetingId) {
        return subscribeToTopic(meetingId + INTERFIX_ACCEPTED + authUser.getId());
    }

    private LiveData<Boolean> subscribeToTopic(String topic) {
        MutableLiveData<Boolean> subData = new MutableLiveData<>();
        messaging.subscribeToTopic(topic)
                .addOnSuccessListener((v) -> {
                    subData.setValue(true);
                })
                .addOnFailureListener(e -> {
                    subData.setValue(false);
                });
        return subData;
    }
}
