package com.github.overpass.gather.screen.meeting.join;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.data.HttpClient;
import com.github.overpass.gather.model.repo.geocode.GeocodeRepo;
import com.github.overpass.gather.model.repo.meeting.JoinRepo;
import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.user.UserRepo;
import com.github.overpass.gather.model.usecase.geo.GeoUseCase;
import com.github.overpass.gather.model.usecase.meeting.MeetingUseCase;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingViewModel;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class JoinViewModel extends BaseMeetingViewModel {

    private final MeetingUseCase meetingUseCase;
    private final GeoUseCase geoUseCase;

    public JoinViewModel() {
        meetingUseCase = new MeetingUseCase(
                new MeetingRepo(FirebaseFirestore.getInstance()),
                new JoinRepo(FirebaseFirestore.getInstance()),
                new UserRepo(FirebaseAuth.getInstance())
        );
        geoUseCase = new GeoUseCase(new GeocodeRepo(HttpClient.getInstance(), new Gson()));
    }

    public LiveData<LoadMeetingStatus> loadMeeting(String meetingId) {
        return meetingUseCase.loadMeeting(meetingId);
    }

    public LiveData<String> getAddress(double latitude, double longitude) {
        return geoUseCase.geoDecode(latitude, longitude);
    }

    public LiveData<JoinStatus> join(String meetingId) {
        return meetingUseCase.join(meetingId);
    }
}
