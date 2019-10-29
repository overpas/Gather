package com.github.overpass.gather.ui.meeting.chat;

import com.github.overpass.gather.commons.abstractions.Sealed;

import java.util.List;

public abstract class MessageModel extends Sealed {

    public static final String SUCCESS = "MessageStatus_SUCCESS";
    public static final String ERROR = "MessageStatus_ERROR";

    public static class Success extends MessageModel {

        private final List<IMessageImpl> messages;

        public Success(List<IMessageImpl> messages) {
            this.messages = messages;
        }

        public List<IMessageImpl> getMessages() {
            return messages;
        }

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Error extends MessageModel {

        private final Throwable throwable;

        public Error(MessageStatus.Error error) {
            this.throwable = error.getThrowable();
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
