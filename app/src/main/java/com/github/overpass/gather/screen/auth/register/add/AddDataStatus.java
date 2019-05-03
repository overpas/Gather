package com.github.overpass.gather.screen.auth.register.add;

import com.github.overpass.gather.model.commons.Sealed;

public abstract class AddDataStatus extends Sealed {

    public static final String SUCCESS = "AddDataStatus_SUCCESS";
    public static final String ERROR = "AddDataStatus_ERROR";
    public static final String PROGRESS = "AddDataStatus_PROGRESS";
    public static final String INVALID_USERNAME = "AddDataStatus_INVALID_USERNAME";

    public static class Success extends AddDataStatus {

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Progress extends AddDataStatus {

        @Override
        public String tag() {
            return PROGRESS;
        }
    }

    public static class InvalidUsername extends AddDataStatus {

        @Override
        public String tag() {
            return INVALID_USERNAME;
        }
    }

    public static class Error extends AddDataStatus {

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
