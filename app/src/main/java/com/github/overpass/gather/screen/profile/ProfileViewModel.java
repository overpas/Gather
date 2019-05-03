package com.github.overpass.gather.screen.profile;

import androidx.core.util.Consumer;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.repo.login.AuthRepo;
import com.github.overpass.gather.model.repo.user.UserDataRepo;
import com.github.overpass.gather.model.usecase.userdata.ProfileUseCase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {

    private final ProfileUseCase profileUseCase;

    public ProfileViewModel() {
        profileUseCase = new ProfileUseCase(
                new UserDataRepo(FirebaseAuth.getInstance()),
                new AuthRepo(FirebaseAuth.getInstance())
        );
    }

    public void getUserData(Consumer<FirebaseUser> onSuccess, Runnable onError) {
        profileUseCase.getUserData(onSuccess, onError);
    }

    public void signOut(Runnable onSuccess) {
        profileUseCase.signOut(onSuccess);
    }
}
