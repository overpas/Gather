package com.github.overpass.gather.model.data.messaging;

public enum MessageType {

    USER_ENROLLED("1"), ACCEPTED("2");

    private final String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
