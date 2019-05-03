package com.github.overpass.gather.model.usecase.userdata;

import androidx.core.util.Consumer;

import com.github.overpass.gather.model.repo.login.AuthRepo;
import com.github.overpass.gather.model.repo.user.UserDataRepo;
import com.google.firebase.auth.FirebaseUser;

public class ProfileUseCase {

    private final UserDataRepo userDataRepo;
    private final AuthRepo authRepo;

    public ProfileUseCase(UserDataRepo userDataRepo, AuthRepo authRepo) {
        this.userDataRepo = userDataRepo;
        this.authRepo = authRepo;
    }

    public void getUserData(Consumer<FirebaseUser> onSuccess, Runnable onError) {
        userDataRepo.getUserData(onSuccess, onError);
    }

    public void signOut(Runnable onSuccess) {
        authRepo.signOut(onSuccess);
    }
}
