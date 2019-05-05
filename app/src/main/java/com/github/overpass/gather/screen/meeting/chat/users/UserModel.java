package com.github.overpass.gather.screen.meeting.chat.users;

import android.net.Uri;

public class UserModel {

    private final String id;
    private final String username;
    private final String email;
    private final Uri uri;

    public UserModel(String id, String username, String email, Uri uri) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Uri getUri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserModel userModel = (UserModel) o;

        if (id != null ? !id.equals(userModel.id) : userModel.id != null) return false;
        if (username != null ? !username.equals(userModel.username) : userModel.username != null)
            return false;
        if (email != null ? !email.equals(userModel.email) : userModel.email != null) return false;
        return uri != null ? uri.equals(userModel.uri) : userModel.uri == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }
}
