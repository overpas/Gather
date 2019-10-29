package com.github.overpass.gather.ui.meeting.join;

import com.github.overpass.gather.ui.meeting.MeetingAndRatio;
import com.github.overpass.gather.ui.meeting.base.LoadMeetingStatus;

public abstract class LoadPrivateMeetingStatus extends LoadMeetingStatus {

    public static final String PROGRESS = "LoadPrivateMeetingStatus_PROGRESS";
    public static final String ERROR = "LoadPrivateMeetingStatus_ERROR";
    public static final String SUCCESS = "LoadPrivateMeetingStatus_SUCCESS";

    public static class Success extends LoadPrivateMeetingStatus {

        private final MeetingAndRatio meetingAndRatio;
        private final boolean isAlreadyEnrolled;

        public Success(MeetingAndRatio meetingAndRatio, boolean isAlreadyEnrolled) {
            this.meetingAndRatio = meetingAndRatio;
            this.isAlreadyEnrolled = isAlreadyEnrolled;
        }

        public MeetingAndRatio getMeetingAndRatio() {
            return meetingAndRatio;
        }

        public boolean isAlreadyEnrolled() {
            return isAlreadyEnrolled;
        }

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Error extends LoadPrivateMeetingStatus {

        @Override
        public String tag() {
            return ERROR;
        }
    }

    public static class Progress extends LoadPrivateMeetingStatus {

        @Override
        public String tag() {
            return PROGRESS;
        }
    }
}
