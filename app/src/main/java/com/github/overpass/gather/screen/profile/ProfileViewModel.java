package com.github.overpass.gather.screen.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.github.overpass.gather.model.repo.login.AuthRepo;
import com.github.overpass.gather.model.repo.pref.PreferenceRepo;
import com.github.overpass.gather.model.repo.user.UserDataRepo;
import com.github.overpass.gather.model.usecase.userdata.ProfileUseCase;
import com.github.overpass.gather.screen.base.personal.DataViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileViewModel extends DataViewModel {

    private final ProfileUseCase profileUseCase;

    private boolean isEditMode = false;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileUseCase = new ProfileUseCase(
                new UserDataRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()),
                new AuthRepo(FirebaseAuth.getInstance()),
                new PreferenceRepo(application)
        );
    }

    public void getUserData(Consumer<FirebaseUser> onSuccess, Runnable onError) {
        profileUseCase.getUserData(onSuccess, onError);
    }

    public void signOut(Runnable onSuccess) {
        profileUseCase.signOut(onSuccess);
    }

    public void onProfileModeChanged(Consumer<Boolean> onChange) {
        isEditMode = !isEditMode;
        onChange.accept(isEditMode);
    }

    public boolean checkIfIsEditMode() {
        boolean value = isEditMode;
        if (isEditMode) {
            isEditMode = false;
        }
        return value;
    }
}
