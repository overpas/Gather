package com.github.overpass.gather.model.commons.exception;

public class NotAuthorized extends Exception {

    public NotAuthorized() {
        this("You are not authorized to access this resource");
    }

    public NotAuthorized(String message) {
        super(message);
    }
}
