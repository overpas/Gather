package com.github.overpass.gather.model.usecase.userdata;

import androidx.core.util.Consumer;

import com.github.overpass.gather.model.repo.login.AuthRepo;
import com.github.overpass.gather.model.repo.pref.PreferenceRepo;
import com.github.overpass.gather.model.repo.user.UserDataRepo;
import com.github.overpass.gather.screen.splash.StartStatus;
import com.google.firebase.auth.FirebaseUser;

public class ProfileUseCase {

    private final UserDataRepo userDataRepo;
    private final AuthRepo authRepo;
    private final PreferenceRepo preferenceRepo;

    public ProfileUseCase(UserDataRepo userDataRepo,
                          AuthRepo authRepo,
                          PreferenceRepo preferenceRepo) {
        this.userDataRepo = userDataRepo;
        this.authRepo = authRepo;
        this.preferenceRepo = preferenceRepo;
    }

    public void getUserData(Consumer<FirebaseUser> onSuccess, Runnable onError) {
        userDataRepo.getCurrentUserData(onSuccess, onError);
    }

    public void signOut(Runnable onSuccess) {
        authRepo.signOut(() -> {
            preferenceRepo.setStartStatus(StartStatus.UNAUTHORIZED);
            onSuccess.run();
        });
    }
}
