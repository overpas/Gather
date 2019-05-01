package com.github.overpass.gather.screen.meeting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.model.usecase.meeting.AllowedUseCase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MeetingViewModel extends ViewModel {

    private final AllowedUseCase allowedUseCase;

    public MeetingViewModel() {
        allowedUseCase = new AllowedUseCase(
                new MeetingRepo(FirebaseFirestore.getInstance()),
                new UserAuthRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
        );
    }

    public LiveData<Boolean> isAllowed(String meetingId) {
        return allowedUseCase.isAllowed(meetingId);
    }
}
