package com.github.overpass.gather.create;

public enum MeetingType {

    BUSINESS(1), ENTERTAINMENT(2), PROTEST(3);

    private final int type;

    MeetingType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
