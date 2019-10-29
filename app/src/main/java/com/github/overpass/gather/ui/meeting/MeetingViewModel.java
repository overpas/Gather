package com.github.overpass.gather.ui.meeting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.usecase.meeting.AllowedUseCase;

import javax.inject.Inject;

public class MeetingViewModel extends ViewModel {

    private final AllowedUseCase allowedUseCase;
    private final String meetingId;

    @Inject
    public MeetingViewModel(AllowedUseCase allowedUseCase, String meetingId) {
        this.allowedUseCase = allowedUseCase;
        this.meetingId = meetingId;
    }

    public LiveData<Boolean> isAllowed() {
        return allowedUseCase.isAllowed(meetingId);
    }
}
