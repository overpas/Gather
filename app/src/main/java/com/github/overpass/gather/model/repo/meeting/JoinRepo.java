package com.github.overpass.gather.model.repo.meeting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.model.commons.exception.NotAuthorized;
import com.github.overpass.gather.screen.map.AuthUser;
import com.github.overpass.gather.screen.meeting.join.JoinStatus;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;

public class JoinRepo implements MeetingsData {

    private final FirebaseFirestore firestore;

    public JoinRepo(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public LiveData<JoinStatus> joinPublic(String meetingId, @Nullable AuthUser authUser) {
        return join(MeetingsData.Users.COLLECTION, new JoinStatus.Joined(), meetingId, authUser);
    }

    public LiveData<JoinStatus> enrollPrivate(String meetingId, @Nullable AuthUser authUser) {
        return join(MeetingsData.PendingUsers.COLLECTION, new JoinStatus.Enrolled(), meetingId, authUser);
    }

    private LiveData<JoinStatus> join(String subcollection,
                                      JoinStatus onSuccessStatus,
                                      String meetingId,
                                      @Nullable AuthUser authUser) {
        MutableLiveData<JoinStatus> joinData = new MutableLiveData<>();
        if (authUser == null) {
            joinData.setValue(new JoinStatus.Error(new NotAuthorized()));
            return joinData;
        }
        joinData.setValue(new JoinStatus.Progress());
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(subcollection)
                .add(authUser)
                .addOnSuccessListener(doc -> joinData.setValue(onSuccessStatus))
                .addOnFailureListener(e -> joinData.setValue(new JoinStatus.Error(e)));
        return joinData;
    }
}
