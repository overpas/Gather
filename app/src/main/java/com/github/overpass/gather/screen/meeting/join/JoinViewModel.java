package com.github.overpass.gather.screen.meeting.join;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.data.HttpClient;
import com.github.overpass.gather.model.repo.geocode.GeocodeRepo;
import com.github.overpass.gather.model.usecase.geo.GeoUseCase;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingViewModel;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;
import com.google.gson.Gson;

public class JoinViewModel extends BaseMeetingViewModel {

    private final GeoUseCase geoUseCase;

    public JoinViewModel() {
        super();
        geoUseCase = new GeoUseCase(new GeocodeRepo(HttpClient.getInstance(), new Gson()));
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
