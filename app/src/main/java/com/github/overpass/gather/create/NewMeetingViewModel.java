package com.github.overpass.gather.create;

import com.github.overpass.gather.map.MeetingRepo;
import com.github.overpass.gather.map.SaveMeetingStatus;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class NewMeetingViewModel extends ViewModel {

    private final CreateMeetingUseCase createMeetingUseCase;

    public NewMeetingViewModel() {
        this.createMeetingUseCase = new CreateMeetingUseCase(
                new MeetingRepo(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance()));
    }

    public LiveData<SaveMeetingStatus> createMeeting(double latitude,
                                                     double longitude,
                                                     String title,
                                                     Date date,
                                                     MeetingType type) {
        return createMeetingUseCase.createMeeting(latitude, longitude, title, date, type);
    }
}
