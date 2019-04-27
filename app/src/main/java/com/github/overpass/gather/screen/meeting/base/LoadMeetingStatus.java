package com.github.overpass.gather.screen.meeting.base;

import com.github.overpass.gather.model.commons.base.Sealed;
import com.github.overpass.gather.screen.meeting.MeetingAndRatio;

public abstract class LoadMeetingStatus extends Sealed {

    public static final String PROGRESS = "LoadMeetingStatus_PROGRESS";
    public static final String ERROR = "LoadMeetingStatus_ERROR";
    public static final String SUCCESS = "LoadMeetingStatus_SUCCESS";

    public static class Success extends LoadMeetingStatus {

        private final MeetingAndRatio meetingAndRatio;

        public Success(MeetingAndRatio meetingAndRatio) {
            this.meetingAndRatio = meetingAndRatio;
        }

        public MeetingAndRatio getMeetingAndRatio() {
            return meetingAndRatio;
        }

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Error extends LoadMeetingStatus {

        @Override
        public String tag() {
            return ERROR;
        }
    }

    public static class Progress extends LoadMeetingStatus {

        @Override
        public String tag() {
            return PROGRESS;
        }
    }
}
