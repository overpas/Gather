package com.github.overpass.gather.screen.meeting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.model.usecase.meeting.AllowedUseCase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

public class MeetingViewModel extends ViewModel {

    private final AllowedUseCase allowedUseCase;

    @Inject
    public MeetingViewModel(AllowedUseCase allowedUseCase) {
        this.allowedUseCase = allowedUseCase;
    }

    public LiveData<Boolean> isAllowed(String meetingId) {
        return allowedUseCase.isAllowed(meetingId);
    }
}
