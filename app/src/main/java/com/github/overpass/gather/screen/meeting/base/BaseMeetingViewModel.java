package com.github.overpass.gather.screen.meeting.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.repo.meeting.JoinRepo;
import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.subscription.SubscriptionRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.model.usecase.meeting.MeetingUseCase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public abstract class BaseMeetingViewModel extends ViewModel {

    protected final MeetingUseCase meetingUseCase;

    public BaseMeetingViewModel() {
        this.meetingUseCase = new MeetingUseCase(
                new MeetingRepo(FirebaseFirestore.getInstance()),
                new JoinRepo(FirebaseFirestore.getInstance()),
                new UserAuthRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()),
                new SubscriptionRepo(FirebaseMessaging.getInstance())
        );
    }

    public LiveData<LoadMeetingStatus> loadMeeting(String meetingId) {
        return meetingUseCase.loadMeeting(meetingId);
    }
}
