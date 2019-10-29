package by.overpass.gather.ui.meeting;

import by.overpass.gather.ui.map.Meeting;
import by.overpass.gather.ui.map.detail.Current2MaxPeopleRatio;

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
