package com.github.overpass.gather.map;

import android.util.Log;

import com.annimon.stream.Stream;
import com.github.overpass.gather.auth.register.add.FailedTask;
import com.github.overpass.gather.create.MeetingType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MeetingRepo {

    private static final String TAG = "MeetingRepo";

    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;

    public MeetingRepo(FirebaseFirestore firestore, FirebaseAuth firebaseAuth) {
        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<List<Meeting>> getMeetings(double latitude, double longitude, double radius) {
        MutableLiveData<List<Meeting>> meetingData = new MutableLiveData<>();
        firestore.collection(Meetings.COLLECTION)
                .whereGreaterThanOrEqualTo(Meetings.FIELD_LATITUDE, latitude - radius)
                .whereLessThanOrEqualTo(Meetings.FIELD_LATITUDE, latitude + radius)
//              .whereGreaterThanOrEqualTo(Meetings.FIELD_LONGITUDE, longitude - radius)
//              .whereLessThanOrEqualTo(Meetings.FIELD_LONGITUDE, longitude + radius)
//              FUCK YOU FIREBASE
                .get()
                .addOnSuccessListener(meetings -> {
                    List<Meeting> list = Stream.of(meetings.toObjects(Meeting.class))
                            .filter(meeting -> meeting.getLongitude() >= longitude - radius
                                    && meeting.getLongitude() <= longitude + radius)
                            .toList();
                    meetingData.setValue(list);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, e.getLocalizedMessage(), e);
                });
        return meetingData;
    }

    public LiveData<SaveMeetingStatus> save(double latitude,
                                            double longitude,
                                            Date date,
                                            String name,
                                            MeetingType type) {
        MutableLiveData<SaveMeetingStatus> resultData = new MutableLiveData<>();
        resultData.setValue(new SaveMeetingStatus.Progress());
        firestore.collection(Meetings.COLLECTION)
                .add(new Meeting(name, latitude, longitude, date, type.getType()))
                .addOnFailureListener(e -> {
                    resultData.setValue(new SaveMeetingStatus.Error(e));
                })
                .continueWithTask(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && firebaseAuth.getCurrentUser() != null) {
                        String id = firebaseAuth.getCurrentUser().getUid();
                        return task.getResult()
                                .collection(Meetings.SUBCOLLECTION_USERS)
                                .add(new AuthUser(AuthUser.Role.ADMIN.getRole(), id));
                    }
                    return new FailedTask<>("Something Went Wrong!");
                })
                .addOnSuccessListener(result -> {
                    resultData.setValue(new SaveMeetingStatus.Success());
                })
                .addOnFailureListener(e -> {
                    resultData.setValue(new SaveMeetingStatus.Error(e));
                });
        return resultData;
    }

    private interface Meetings {

        String COLLECTION = "Meetings";
        String SUBCOLLECTION_USERS = "Users";
        String FIELD_LATITUDE = "latitude";
        String FIELD_LONGITUDE = "longitude";
    }
}
