package com.github.overpass.gather.model.repo.meeting;

public interface MeetingsData {

    String COLLECTION_MEETINGS = "Meetings";
    String FIELD_LATITUDE = "latitude";
    String FIELD_LONGITUDE = "longitude";
    String FIELD_NAME = "name";
    String FIELD_PHOTOS = "photos";

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

    interface Messages {
        String COLLECTION = "Messages";
        String FIELD_TEXT = "text";
        String FIELD_DATE = "date";
        String FIELD_AUTHOR_ID = "authorId";
        String FIELD_AUTHOR_NAME = "authorName";
        String FIELD_AUTHOR_PHOTO_URL = "authorPhotoUrl";
    }
}