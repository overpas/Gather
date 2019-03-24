package com.github.overpass.gather.auth.register.add;

import com.github.overpass.gather.base.Sealed;

public abstract class ImageUploadStatus extends Sealed {

    private static final String ERROR = "ImageUploadStatus_ERROR";
    private static final String SUCCESS = "ImageUploadStatus_SUCCESS";
    private static final String PROGRESS = "ImageUploadStatus_PROGRESS";

    public static class Error extends ImageUploadStatus {

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

    public static class Success extends ImageUploadStatus {

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Progress extends ImageUploadStatus {

        private long percent;

        public Progress(long percent) {
            this.percent = percent;
        }

        public long getPercent() {
            return percent;
        }

        @Override
        public String tag() {
            return PROGRESS;
        }
    }
}
