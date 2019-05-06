package com.github.overpass.gather.model.repo.meeting;

import com.github.overpass.gather.screen.auth.register.signup.User;

public interface MeetingsData {

    String COLLECTION_MEETINGS = "Meetings";
    String FIELD_LATITUDE = "latitude";
    String FIELD_LONGITUDE = "longitude";

    interface Users {
        String COLLECTION = "Users";
        String FIELD_ID = "id";
        String FIELD_ROLE = "role";
    }

    interface PendingUsers {
        String COLLECTION = "PendingUsers";
        String FIELD_ID = Users.FIELD_ID;
        String FIELD_ROLE = Users.FIELD_ROLE;
    }
}