package com.github.overpass.gather.screen.meeting.chat.users;

import com.github.overpass.gather.model.commons.Sealed;

import java.util.List;

public abstract class LoadUsersStatus extends Sealed {

    public static final String PROGRESS = "LoadUsersStatus_PROGRESS";
    public static final String ERROR = "LoadUsersStatus_ERROR";
    public static final String PENDING_SUCCESS = "LoadUsersStatus_PENDING_SUCCESS";
    public static final String SUCCESS = "LoadUsersStatus_SUCCESS";
    public static final String MEMBERS_SUCCESS = "LoadUsersStatus_MEMBERS_SUCCESS";

    public static class Progress extends LoadUsersStatus {

        @Override
        public String tag() {
            return PROGRESS;
        }
    }

    public static class Error extends LoadUsersStatus {

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

    public static abstract class Success extends LoadUsersStatus {

        private final List<UserModel> members;

        public Success(List<UserModel> members) {
            this.members = members;
        }

        public List<UserModel> getMembers() {
            return members;
        }

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class MembersSuccess extends Success {

        public MembersSuccess(List<UserModel> members) {
            super(members);
        }

        @Override
        public String tag() {
            return MEMBERS_SUCCESS;
        }
    }

    public static class PendingSuccess extends Success {

        public PendingSuccess(List<UserModel> members) {
            super(members);
        }

        @Override
        public String tag() {
            return PENDING_SUCCESS;
        }
    }
}
