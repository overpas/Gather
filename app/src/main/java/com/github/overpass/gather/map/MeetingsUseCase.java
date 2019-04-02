package com.github.overpass.gather.map;

import android.location.Location;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MeetingsUseCase {

    private static final double RADIUS = 0.02; // approximately 1 mile

    private final MeetingRepo meetingRepo;

    public MeetingsUseCase(MeetingRepo meetingRepo) {
        this.meetingRepo = meetingRepo;
    }

    public LiveData<List<Meeting>> getMeetings(Location location) {
        return meetingRepo.getMeetings(location.getLatitude(), location.getLongitude(), RADIUS);
    }
}
