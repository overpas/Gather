package com.github.overpass.gather.create;

import android.text.TextUtils;

import com.github.overpass.gather.map.MeetingRepo;
import com.github.overpass.gather.map.SaveMeetingStatus;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

class CreateMeetingUseCase {

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
