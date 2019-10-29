package by.overpass.gather.model.repo.meeting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import by.overpass.gather.commons.exception.NotAuthorized;
import by.overpass.gather.ui.map.AuthUser;
import by.overpass.gather.ui.meeting.join.JoinStatus;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;
import javax.inject.Inject;

import by.overpass.gather.commons.exception.NotAuthorized;

public class JoinRepo implements MeetingsMetadata {

    private final FirebaseFirestore firestore;

    @Inject
    public JoinRepo(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public LiveData<JoinStatus> joinPublic(String meetingId, @Nullable AuthUser authUser) {
        return join(MeetingsMetadata.Users.COLLECTION, new JoinStatus.Joined(), meetingId, authUser);
    }

    public LiveData<JoinStatus> enrollPrivate(String meetingId, @Nullable AuthUser authUser) {
        return join(MeetingsMetadata.PendingUsers.COLLECTION, new JoinStatus.Enrolled(), meetingId,
                authUser);
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
