package com.github.overpass.gather.screen.auth.login.forgot;

import com.github.overpass.gather.model.commons.base.Sealed;

public abstract class ForgotStatus extends Sealed {

    public static final String ERROR = "ForgotStatus_ERROR";
    public static final String SUCCESS = "ForgotStatus_SUCCESS";
    public static final String PROGRESS = "ForgotStatus_PROGRESS";
    public static final String INVALID_EMAIL = "ForgotStatus_INVALID_EMAIL";

    private ForgotStatus() {
    }

    public static class Error extends ForgotStatus {

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

    public static class InvalidEmail extends ForgotStatus {

        @Override
        public String tag() {
            return INVALID_EMAIL;
        }
    }

    public static class Success extends ForgotStatus {
        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Progress extends ForgotStatus {
        @Override
        public String tag() {
            return PROGRESS;
        }
    }
}
