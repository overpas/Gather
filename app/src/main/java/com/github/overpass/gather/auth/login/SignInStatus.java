package com.github.overpass.gather.auth.login;

import com.github.overpass.gather.model.commons.base.Sealed;

public abstract class SignInStatus extends Sealed {

    public static final String ERROR = "SignInStatus_ERROR";
    public static final String SUCCESS = "SignInStatus_SUCCESS";
    public static final String PROGRESS = "SignInStatus_PROGRESS";
    public static final String INVALID_EMAIL = "SignInStatus_INVALID_EMAIL";
    public static final String INVALID_PASSWORD = "SignInStatus_INVALID_PASSWORD";

    private SignInStatus() {
    }

    public static class Error extends SignInStatus {

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

    public static class InvalidEmail extends SignInStatus {

        private final String message;

        public InvalidEmail(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String tag() {
            return INVALID_EMAIL;
        }
    }

    public static class InvalidPassword extends SignInStatus {

        private final String message;

        public InvalidPassword(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String tag() {
            return INVALID_PASSWORD;
        }
    }

    public static class Success extends SignInStatus {
        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Progress extends SignInStatus {
        @Override
        public String tag() {
            return PROGRESS;
        }
    }
}
