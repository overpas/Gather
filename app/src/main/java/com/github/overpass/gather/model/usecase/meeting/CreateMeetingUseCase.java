package com.github.overpass.gather.model.usecase.meeting;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.screen.create.MeetingType;
import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.screen.map.SaveMeetingStatus;

import java.util.Date;

public class CreateMeetingUseCase {

    private final MeetingRepo meetingRepo;

    public CreateMeetingUseCase(MeetingRepo meetingRepo) {
        this.meetingRepo = meetingRepo;
    }

    public LiveData<SaveMeetingStatus> createMeeting(double latitude,
                                                     double longitude,
                                                     String title,
                                                     Date date,
                                                     MeetingType type,
                                                     int maxPeople,
                                                     boolean isPrivate) {
        if (TextUtils.isEmpty(title)) {
            MutableLiveData<SaveMeetingStatus> resultData = new MutableLiveData<>();
            resultData.setValue(new SaveMeetingStatus.EmptyName());
            return resultData;
        }
        return meetingRepo.save(latitude, longitude, date, title, type, maxPeople, isPrivate);
    }
}
