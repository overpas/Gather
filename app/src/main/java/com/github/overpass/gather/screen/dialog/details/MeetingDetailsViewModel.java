package com.github.overpass.gather.screen.dialog.details;

import android.content.Context;

import com.github.overpass.gather.model.commons.export.EventExportHelper;
import com.github.overpass.gather.screen.meeting.MeetingAndRatio;
import com.github.overpass.gather.screen.meeting.join.JoinViewModel;

public class MeetingDetailsViewModel extends JoinViewModel {

    private final EventExportHelper eventExportHelper;

    public MeetingDetailsViewModel() {
        this.eventExportHelper = new EventExportHelper();
    }

    public void exportEvent(MeetingAndRatio meetingAndRatio, Context context) {
        eventExportHelper.exportEventToCalendar(meetingAndRatio, context);
    }
}
