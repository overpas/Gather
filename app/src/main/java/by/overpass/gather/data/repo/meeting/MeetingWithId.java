package by.overpass.gather.data.repo.meeting;

import by.overpass.gather.ui.map.Meeting;

public class MeetingWithId {

    private Meeting meeting;
    private String id;

    public MeetingWithId(Meeting meeting, String id) {
        this.meeting = meeting;
        this.id = id;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public String getId() {
        return id;
    }
}
