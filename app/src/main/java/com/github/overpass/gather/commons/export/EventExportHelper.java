package com.github.overpass.gather.commons.export;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

import com.github.overpass.gather.R;
import com.github.overpass.gather.ui.create.MeetingType;
import com.github.overpass.gather.ui.meeting.MeetingAndRatio;

import javax.inject.Inject;

public class EventExportHelper {

    private static final String MAPS_LINK_TEMPLATE = "http://www.google.com/maps/place/%s,%s";

    private final Context context;

    @Inject
    public EventExportHelper(Context context) {
        this.context = context;
    }

    public void exportEventToCalendar(MeetingAndRatio meetingAndRatio) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        addTime(intent, meetingAndRatio);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.TITLE, meetingAndRatio.getMeeting().getName());
        addDescription(intent, meetingAndRatio, context);
        addLocation(intent, meetingAndRatio);
        context.startActivity(intent);
    }

    private void addTime(Intent intent, MeetingAndRatio meetingAndRatio) {
        long time = meetingAndRatio.getMeeting().getDate().getTime();
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time);
    }

    private void addDescription(Intent intent, MeetingAndRatio meetingAndRatio, Context context) {
        StringBuilder builder = new StringBuilder();
        boolean isPrivate = meetingAndRatio.getMeeting().isPrivate();
        String type;
        if (MeetingType.isEntertainment(meetingAndRatio.getMeeting().getType())) {
            type = context.getString(R.string.export_entertainment);
        } else if (MeetingType.isProtest(meetingAndRatio.getMeeting().getType())) {
            type = context.getString(R.string.export_protest);
        } else {
            type = context.getString(R.string.business);
        }
        int max = meetingAndRatio.getRatio().getMax();
        builder.append(context.getString(R.string.export_indefinite_article))
                .append(isPrivate ? context.getString(R.string.export_private)
                        : context.getString(R.string.export_public))
                .append(" ")
                .append(type)
                .append(context.getString(R.string.export_meeting))
                .append(context.getString(R.string.export_including))
                .append(max)
                .append(context.getString(R.string.export_maximum))
                .append('.');
        intent.putExtra(CalendarContract.Events.DESCRIPTION, builder.toString());
    }

    private void addLocation(Intent intent, MeetingAndRatio meetingAndRatio) {
        String linkToGoogleMaps = String.format(
                MAPS_LINK_TEMPLATE,
                meetingAndRatio.getMeeting().getLatitude(),
                meetingAndRatio.getMeeting().getLongitude()
        );
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, linkToGoogleMaps);
    }
}
