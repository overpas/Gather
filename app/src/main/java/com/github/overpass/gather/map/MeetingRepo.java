package com.github.overpass.gather.map;

import com.github.overpass.gather.auth.register.add.FailedTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MeetingRepo {

    private static final String MEETINGS = "Meetings";
    private static final String USERS = "Users";

    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;

    public MeetingRepo(FirebaseFirestore firestore, FirebaseAuth firebaseAuth) {
        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<List<Meeting>> getMeetings(double latitude, double longitude, int radius) {
        return new MutableLiveData<>();
    }

    public LiveData<SaveMeetingStatus> save(double latitude,
                                            double longitude,
                                            Date date,
                                            String name) {
        MutableLiveData<SaveMeetingStatus> resultData = new MutableLiveData<>();
        resultData.setValue(new SaveMeetingStatus.Progress());
        firestore.collection(MEETINGS)
                .add(new Meeting(name, latitude, longitude, date))
                .addOnFailureListener(e -> {
                    resultData.setValue(new SaveMeetingStatus.Error(e));
                })
                .continueWithTask(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && firebaseAuth.getCurrentUser() != null) {
                        String id = firebaseAuth.getCurrentUser().getUid();
                        return task.getResult()
                                .collection(USERS)
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
}
