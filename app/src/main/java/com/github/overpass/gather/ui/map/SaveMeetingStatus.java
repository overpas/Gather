package com.github.overpass.gather.ui.map;

import com.github.overpass.gather.commons.abstractions.Sealed;

public abstract class SaveMeetingStatus extends Sealed {

    public static final String PROGRESS = "SaveMeetingStatus_PROGRESS";
    public static final String SUCCESS = "SaveMeetingStatus_SUCCESS";
    public static final String ERROR = "SaveMeetingStatus_ERROR";
    public static final String EMPTY_NAME = "SaveMeetingStatus_EMPTY_NAME";

    public static final class Progress extends SaveMeetingStatus {

        @Override
        public String tag() {
            return PROGRESS;
        }
    }

    public static final class Success extends SaveMeetingStatus {

        private final String meetingId;

        public Success(String meetingId) {
            this.meetingId = meetingId;
        }

        public String getMeetingId() {
            return meetingId;
        }

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static final class Error extends SaveMeetingStatus {

        private final Throwable throwable;

        public Error(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        @Override
        public String tag() {
            return ERROR;
        }
    }

    public static final class EmptyName extends SaveMeetingStatus {

        @Override
        public String tag() {
            return EMPTY_NAME;
        }
    }
}
