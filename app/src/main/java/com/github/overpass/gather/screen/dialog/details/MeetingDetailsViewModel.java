package com.github.overpass.gather.screen.dialog.details;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.commons.export.EventExportHelper;
import com.github.overpass.gather.model.usecase.geo.GeoUseCase;
import com.github.overpass.gather.model.usecase.meeting.MeetingUseCase;
import com.github.overpass.gather.screen.meeting.MeetingAndRatio;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingViewModel;

import javax.inject.Inject;

public class MeetingDetailsViewModel extends BaseMeetingViewModel {

    private final GeoUseCase geoUseCase;
    private final EventExportHelper eventExportHelper;

    @Inject
    public MeetingDetailsViewModel(GeoUseCase geoUseCase,
                                   EventExportHelper eventExportHelper,
                                   MeetingUseCase meetingUseCase) {
        super(meetingUseCase);
        this.geoUseCase = geoUseCase;
        this.eventExportHelper = eventExportHelper;
    }

    LiveData<String> getAddress(double latitude, double longitude) {
        return geoUseCase.geoDecode(latitude, longitude);
    }

    void exportEvent(MeetingAndRatio meetingAndRatio, Context context) {
        eventExportHelper.exportEventToCalendar(meetingAndRatio, context);
    }
}
