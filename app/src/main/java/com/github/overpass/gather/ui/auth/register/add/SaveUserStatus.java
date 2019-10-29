package com.github.overpass.gather.ui.auth.register.add;

import com.github.overpass.gather.commons.abstractions.Sealed;

public abstract class SaveUserStatus extends Sealed {

    public static final String SUCCESS = "SaveUserStatus_SUCCESS";
    public static final String ERROR = "SaveUserStatus_ERROR";
    public static final String PROGRESS = "SaveUserStatus_PROGRESS";

    public static class Success extends SaveUserStatus {

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Progress extends SaveUserStatus {

        @Override
        public String tag() {
            return PROGRESS;
        }
    }

    public static class Error extends SaveUserStatus {

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
}
