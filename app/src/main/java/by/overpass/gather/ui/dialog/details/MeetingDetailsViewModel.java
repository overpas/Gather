package by.overpass.gather.ui.dialog.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import by.overpass.gather.commons.export.EventExportHelper;
import by.overpass.gather.model.usecase.geo.GeoUseCase;
import by.overpass.gather.model.usecase.meeting.MeetingUseCase;
import by.overpass.gather.ui.meeting.MeetingAndRatio;
import by.overpass.gather.ui.meeting.base.LoadMeetingStatus;

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
