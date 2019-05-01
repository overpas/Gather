package com.github.overpass.gather.screen.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.subscription.SubscriptionRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.screen.map.SaveMeetingStatus;
import com.github.overpass.gather.model.usecase.meeting.CreateMeetingUseCase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Date;

public class NewMeetingViewModel extends ViewModel {

    private final CreateMeetingUseCase createMeetingUseCase;

    public NewMeetingViewModel() {
        this.createMeetingUseCase = new CreateMeetingUseCase(
                new MeetingRepo(FirebaseFirestore.getInstance()),
                new UserAuthRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()),
                new SubscriptionRepo(FirebaseMessaging.getInstance())
        );
    }

    public LiveData<SaveMeetingStatus> createMeeting(double latitude,
                                                     double longitude,
                                                     String title,
                                                     Date date,
                                                     MeetingType type,
                                                     int maxPeople,
                                                     boolean isPrivate) {
        return createMeetingUseCase.createMeeting(latitude, longitude, title, date, type,
                maxPeople, isPrivate);
    }
}
