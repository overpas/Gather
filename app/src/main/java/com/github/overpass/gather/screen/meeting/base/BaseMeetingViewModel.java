package com.github.overpass.gather.screen.meeting.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.usecase.meeting.MeetingUseCase;

public abstract class BaseMeetingViewModel extends ViewModel {

    protected final MeetingUseCase meetingUseCase;

    public BaseMeetingViewModel(MeetingUseCase meetingUseCase) {
        this.meetingUseCase = meetingUseCase;
    }

    public LiveData<LoadMeetingStatus> loadMeeting(String meetingId) {
        return meetingUseCase.loadMeeting(meetingId);
    }
}
