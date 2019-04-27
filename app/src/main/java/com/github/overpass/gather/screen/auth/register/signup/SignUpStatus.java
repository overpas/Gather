package com.github.overpass.gather.screen.auth.register.signup;

import com.github.overpass.gather.model.commons.base.Sealed;

public abstract class SignUpStatus extends Sealed {

    public static final String ERROR = "SignUpStatus_ERROR";
    public static final String SUCCESS = "SignUpStatus_SUCCESS";
    public static final String PROGRESS = "SignUpStatus_PROGRESS";
    public static final String INVALID_EMAIL = "SignUpStatus_INVALID_EMAIL";
    public static final String INVALID_PASSWORD = "SignUpStatus_INVALID_PASSWORD";

    public SignUpStatus() {
    }

    public static class Error extends SignUpStatus {

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

    public static class InvalidEmail extends SignUpStatus {

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

    public static class InvalidPassword extends SignUpStatus {

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

    public static class Success extends SignUpStatus {
        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Progress extends SignUpStatus {
        @Override
        public String tag() {
            return PROGRESS;
        }
    }
}
