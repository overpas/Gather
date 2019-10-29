package com.github.overpass.gather.ui.meeting.chat.users.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.overpass.gather.commons.abstractions.Sealed;

import java.util.ArrayList;
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

        public Success(@Nullable List<UserModel> members) {
            this.members = members;
        }

        @NonNull
        public List<UserModel> getUsers() {
            return members == null ? new ArrayList<>() : members;
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
