package com.github.overpass.gather.ui.meeting.join;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.usecase.geo.GeoUseCase;
import com.github.overpass.gather.model.usecase.meeting.MeetingUseCase;

import javax.inject.Inject;

public class JoinViewModel extends ViewModel {

    private final MeetingUseCase meetingUseCase;
    private final GeoUseCase geoUseCase;
    private final String meetingId;

    @Inject
    public JoinViewModel(MeetingUseCase meetingUseCase,
                         GeoUseCase geoUseCase,
                         String meetingId) {
        this.meetingUseCase = meetingUseCase;
        this.geoUseCase = geoUseCase;
        this.meetingId = meetingId;
    }

    public LiveData<String> getAddress(double latitude, double longitude) {
        return geoUseCase.geoDecode(latitude, longitude);
    }

    public LiveData<JoinStatus> join() {
        return meetingUseCase.join(meetingId);
    }

    public LiveData<LoadPrivateMeetingStatus> loadMeetingCheckEnrolled() {
        return meetingUseCase.loadMeetingCheckEnrolled(meetingId);
    }
}
