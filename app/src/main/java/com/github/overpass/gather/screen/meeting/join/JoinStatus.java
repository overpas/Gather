package com.github.overpass.gather.screen.meeting.join;

import com.github.overpass.gather.model.commons.base.Sealed;

public abstract class JoinStatus extends Sealed {
    
    public static final String ERROR = "JoinStatus_ERROR";
    public static final String PROGRESS = "JoinStatus_PROGRESS";
    public static final String ENROLLED = "JoinStatus_ENROLLED";
    public static final String JOINED = "JoinStatus_JOINED";

    public static final class Error extends JoinStatus {

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

    public static final class Progress extends JoinStatus {

        @Override
        public String tag() {
            return PROGRESS;
        }
    }

    public static final class Enrolled extends JoinStatus {

        @Override
        public String tag() {
            return ENROLLED;
        }
    }

    public static final class Joined extends JoinStatus {

        @Override
        public String tag() {
            return JOINED;
        }
    }
}
