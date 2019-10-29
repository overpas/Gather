package com.github.overpass.gather.ui.meeting.chat;

import com.github.overpass.gather.commons.abstractions.Sealed;
import com.github.overpass.gather.model.repo.message.Message;

import java.util.List;

public abstract class MessageStatus extends Sealed {

    public static final String SUCCESS = "MessageStatus_SUCCESS";
    public static final String ERROR = "MessageStatus_ERROR";

    public static class Success extends MessageStatus {

        private final List<Message> messages;

        public Success(List<Message> messages) {
            this.messages = messages;
        }

        public List<Message> getMessages() {
            return messages;
        }

        @Override
        public String tag() {
            return SUCCESS;
        }
    }

    public static class Error extends MessageStatus {

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
