package com.github.overpass.gather.model.usecase.meeting;

import android.location.Location;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.screen.map.Meeting;
import com.github.overpass.gather.screen.map.detail.Current2MaxPeopleRatio;
import com.github.overpass.gather.model.repo.meeting.MeetingRepo;

import java.util.Map;

public class MeetingsUseCase {

    private static final double RADIUS = 0.02; // approximately 1 mile

    private final MeetingRepo meetingRepo;

    public MeetingsUseCase(MeetingRepo meetingRepo) {
        this.meetingRepo = meetingRepo;
    }

    public LiveData<Map<String, Meeting>> getMeetings(Location location) {
        return meetingRepo.getMeetings(location.getLatitude(), location.getLongitude(), RADIUS);
    }

    public LiveData<Current2MaxPeopleRatio> getCurrent2MaxPeopleRatio(String id) {
        return meetingRepo.getCurrent2MaxRatio(id);
    }
}
