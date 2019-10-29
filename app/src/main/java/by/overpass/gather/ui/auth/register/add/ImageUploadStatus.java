package by.overpass.gather.ui.auth.register.add;

import android.net.Uri;

import by.overpass.gather.commons.abstractions.Sealed;

public abstract class ImageUploadStatus extends Sealed {

    public static final String ERROR = "ImageUploadStatus_ERROR";
    public static final String SUCCESS = "ImageUploadStatus_SUCCESS";
    public static final String PROGRESS = "ImageUploadStatus_PROGRESS";

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

        private final Uri uri;

        public Success(Uri uri) {
            this.uri = uri;
        }

        public Uri getUri() {
            return uri;
        }

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
