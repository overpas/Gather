package com.github.overpass.gather.screen.meeting.join;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.usecase.geo.GeoUseCase;
import com.github.overpass.gather.model.usecase.meeting.MeetingUseCase;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingViewModel;

import javax.inject.Inject;

public class JoinViewModel extends BaseMeetingViewModel {

    private final GeoUseCase geoUseCase;

    @Inject
    public JoinViewModel(MeetingUseCase meetingUseCase, GeoUseCase geoUseCase) {
        super(meetingUseCase);
        this.geoUseCase = geoUseCase;
    }

    public LiveData<String> getAddress(double latitude, double longitude) {
        return geoUseCase.geoDecode(latitude, longitude);
    }

    public LiveData<JoinStatus> join(String meetingId) {
        return meetingUseCase.join(meetingId);
    }

    public LiveData<LoadPrivateMeetingStatus> loadMeetingCheckEnrolled(String meetingId) {
        return meetingUseCase.loadMeetingCheckEnrolled(meetingId);
    }
}
