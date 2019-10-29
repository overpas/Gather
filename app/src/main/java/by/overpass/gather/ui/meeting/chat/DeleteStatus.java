package by.overpass.gather.ui.meeting.chat;

import by.overpass.gather.commons.abstractions.Sealed;

public abstract class DeleteStatus extends Sealed {

    public static final String SUCCESS = "DeleteStatus_SUCCESS";
    public static final String ERROR = "DeleteStatus_ERROR";
    public static final String PROGRESS = "DeleteStatus_PROGRESS";

    public static class Success extends DeleteStatus {

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Error extends DeleteStatus {

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

    public static class Progress extends DeleteStatus {

        @Override
        public String tag() {
            return PROGRESS;
        }
    }
}
