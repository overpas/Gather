package com.github.overpass.gather.auth.register.confirm;

import com.github.overpass.gather.base.Sealed;

abstract class ConfirmEmailStatus extends Sealed {

    public static final String SUCCESS = "ConfirmEmailStatus_SUCCESS";
    public static final String ERROR = "ConfirmEmailStatus_ERROR";
    public static final String FAIL = "ConfirmEmailStatus_FAIL";
    public static final String PROGRESS = "ConfirmEmailStatus_PROGRESS";

    static class Success extends ConfirmEmailStatus {

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    static class Error extends ConfirmEmailStatus {

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

    static class Fail extends ConfirmEmailStatus {

        private final String message;

        public Fail(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String tag() {
            return FAIL;
        }
    }

    static class Progress extends ConfirmEmailStatus {

        @Override
        public String tag() {
            return PROGRESS;
        }
    }

}
