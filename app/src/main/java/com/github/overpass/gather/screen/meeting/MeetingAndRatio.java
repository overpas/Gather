package com.github.overpass.gather.screen.meeting;

import com.github.overpass.gather.screen.map.Meeting;
import com.github.overpass.gather.screen.map.detail.Current2MaxPeopleRatio;

public class MeetingAndRatio {

    private final Meeting meeting;
    private final Current2MaxPeopleRatio ratio;

    public MeetingAndRatio(Meeting meeting, Current2MaxPeopleRatio ratio) {
        this.meeting = meeting;
        this.ratio = ratio;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public Current2MaxPeopleRatio getRatio() {
        return ratio;
    }
}
