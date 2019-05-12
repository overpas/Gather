package com.github.overpass.gather.model.commons.exception;

public class PhotoUploadException extends Exception {

    public PhotoUploadException() {
        this("Couldn't upload photo");
    }

    public PhotoUploadException(String message) {
        super(message);
    }
}
