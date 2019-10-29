package com.github.overpass.gather.model.usecase.search;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.meeting.MeetingWithId;

import java.util.List;

import javax.inject.Inject;

public class SearchUseCase {

    private final MeetingRepo meetingRepo;

    @Inject
    public SearchUseCase(MeetingRepo meetingRepo) {
        this.meetingRepo = meetingRepo;
    }

    public LiveData<List<MeetingWithId>> searchByName(String name) {
        return meetingRepo.searchByName(name);
    }
}
