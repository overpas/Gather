package com.github.overpass.gather.screen.meeting.chat.attachments;

import com.github.overpass.gather.model.commons.Sealed;

public abstract class PhotoUploadStatus extends Sealed {

    public static final String SUCCESS = "PhotoUploadStatus_SUCCESS";
    public static final String ERROR = "PhotoUploadStatus_ERROR";
    public static final String PROGRESS = "PhotoUploadStatus_PROGRESS";

    public static class Success extends PhotoUploadStatus {

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Error extends PhotoUploadStatus {

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

    public static class Progress extends PhotoUploadStatus {

        @Override
        public String tag() {
            return PROGRESS;
        }
    }
}
