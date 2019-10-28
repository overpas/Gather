package com.github.overpass.gather.screen.dialog.details;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.commons.export.EventExportHelper;
import com.github.overpass.gather.model.usecase.geo.GeoUseCase;
import com.github.overpass.gather.model.usecase.meeting.MeetingUseCase;
import com.github.overpass.gather.screen.meeting.MeetingAndRatio;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;

import javax.inject.Inject;

public class MeetingDetailsViewModel extends ViewModel {

    private final MeetingUseCase meetingUseCase;
    private final GeoUseCase geoUseCase;
    private final EventExportHelper eventExportHelper;
    private final String meetingId;

    @Inject
    public MeetingDetailsViewModel(GeoUseCase geoUseCase,
                                   EventExportHelper eventExportHelper,
                                   MeetingUseCase meetingUseCase,
                                   String meetingId) {
        this.meetingUseCase = meetingUseCase;
        this.geoUseCase = geoUseCase;
        this.eventExportHelper = eventExportHelper;
        this.meetingId = meetingId;
    }

    LiveData<String> getAddress(double latitude, double longitude) {
        return geoUseCase.geoDecode(latitude, longitude);
    }

    void exportEvent(MeetingAndRatio meetingAndRatio) {
        eventExportHelper.exportEventToCalendar(meetingAndRatio);
    }

    public LiveData<LoadMeetingStatus> loadMeeting() {
        return meetingUseCase.loadMeeting(meetingId);
    }
}
