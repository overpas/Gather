package com.github.overpass.gather.model.repo.user;

import com.google.firebase.firestore.FirebaseFirestore;

public class UserDataRepo {

    private final FirebaseFirestore firestore;

    public UserDataRepo(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }
}
