package com.github.overpass.gather.map;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MeetingRepo {

    private final FirebaseFirestore firestore;

    public MeetingRepo(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public LiveData<List<Meeting>> getMeetings(double latitude, double longitude, int radius) {
        return new MutableLiveData<>();
    }
}
