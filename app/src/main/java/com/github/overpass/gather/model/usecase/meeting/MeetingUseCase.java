package com.github.overpass.gather.model.usecase.meeting;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;

public class MeetingUseCase {

    private final MeetingRepo meetingRepo;

    public MeetingUseCase(MeetingRepo meetingRepo) {
        this.meetingRepo = meetingRepo;
    }

    public LiveData<LoadMeetingStatus> loadMeeting(String meetingId) {
        return meetingRepo.getFullMeeting(meetingId);
    }
}
