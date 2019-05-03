package com.github.overpass.gather.model.repo.user;

import androidx.core.util.Consumer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDataRepo {

    private final FirebaseAuth auth;

    public UserDataRepo(FirebaseAuth auth) {
        this.auth = auth;
    }

    public void getUserData(Consumer<FirebaseUser> onSuccess, Runnable onError) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            onSuccess.accept(user);
        } else {
            onError.run();
        }
    }
}
