package com.github.overpass.gather.map;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.annimon.stream.Stream;
import com.github.overpass.gather.auth.register.add.FailedTask;
import com.github.overpass.gather.create.MeetingType;
import com.github.overpass.gather.map.detail.Current2MaxPeopleRatio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MeetingRepo {

    private static final String TAG = "MeetingRepo";

    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;

    public MeetingRepo(FirebaseFirestore firestore, FirebaseAuth firebaseAuth) {
        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<Map<String, Meeting>> getMeetings(double latitude, double longitude, double radius) {
        MutableLiveData<Map<String, Meeting>> meetingData = new MutableLiveData<>();
        firestore.collection(Meetings.COLLECTION)
                .whereGreaterThanOrEqualTo(Meetings.FIELD_LATITUDE, latitude - radius)
                .whereLessThanOrEqualTo(Meetings.FIELD_LATITUDE, latitude + radius)
//              .whereGreaterThanOrEqualTo(Meetings.FIELD_LONGITUDE, longitude - radius)
//              .whereLessThanOrEqualTo(Meetings.FIELD_LONGITUDE, longitude + radius)
//              FUCK YOU FIREBASE
                .get()
                .addOnSuccessListener(docs -> {
                    Map<String, Meeting> map = Stream.of(docs.getDocuments())
                            .custom(documentSnapshotStream -> {
                                Map<String, Meeting> meetingMap = new HashMap<>();
                                documentSnapshotStream.forEach(documentSnapshot -> {
                                    meetingMap.put(documentSnapshot.getId(),
                                            documentSnapshot.toObject(Meeting.class));
                                });
                                return meetingMap;
                            });
                    meetingData.setValue(map);
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
                                            MeetingType type,
                                            int maxPeople,
                                            boolean isPrivate) {
        MutableLiveData<SaveMeetingStatus> resultData = new MutableLiveData<>();
        resultData.setValue(new SaveMeetingStatus.Progress());
        firestore.collection(Meetings.COLLECTION)
                .add(new Meeting(name, latitude, longitude, date, type.getType(), maxPeople,
                        isPrivate))
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

    public LiveData<Current2MaxPeopleRatio> getCurrent2MaxRatio(String id) {
        MutableLiveData<Current2MaxPeopleRatio> maxPeopleData = new MutableLiveData<>();
        final int[] numbers = new int[2];
        firestore.collection(Meetings.COLLECTION)
                .document(id)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        numbers[0] = (int) task.getResult().getLong("maxPeople").longValue();
                        return task.getResult()
                                .getReference()
                                .collection(Meetings.SUBCOLLECTION_USERS)
                                .get();
                    }
                    return new FailedTask<>("Something went wrong");
                })
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    numbers[1] = queryDocumentSnapshots.size();
                    maxPeopleData.setValue(new Current2MaxPeopleRatio(numbers[1], numbers[0]));
                });
        return maxPeopleData;
    }

    private interface Meetings {

        String COLLECTION = "Meetings";
        String SUBCOLLECTION_USERS = "Users";
        String FIELD_LATITUDE = "latitude";
        String FIELD_LONGITUDE = "longitude";
    }
}
