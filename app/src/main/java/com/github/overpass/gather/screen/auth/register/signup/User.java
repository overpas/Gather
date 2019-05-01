package com.github.overpass.gather.screen.auth.register.signup;

import javax.annotation.Nullable;

public class User {

    private String email;
    @Nullable
    private String username;
    @Nullable
    private String photoUrl;

    public User(String email, @Nullable String username, @Nullable String photoUrl) {
        this.email = email;
        this.username = username;
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(@Nullable String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
