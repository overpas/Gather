package com.github.overpass.gather.model.commons.exception;

public class SubscriptionException extends Throwable {

    public SubscriptionException() {
        this("Couldn't subscribe to neccessary topic");
    }

    public SubscriptionException(String message) {
        super(message);
    }
}