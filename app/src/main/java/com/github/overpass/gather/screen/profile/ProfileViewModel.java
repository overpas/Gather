package com.github.overpass.gather.screen.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.model.usecase.userdata.ProfileUseCase;
import com.github.overpass.gather.screen.base.personal.DataViewModel;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class ProfileViewModel extends DataViewModel {

    private final ProfileUseCase profileUseCase;

    private boolean isEditMode = false;

    @Inject
    public ProfileViewModel(@NonNull Application application,
                            ProfileUseCase profileUseCase,
                            ImageSourceUseCase imageSourceUseCase) {
        super(application);
        setImageSourceUseCase(imageSourceUseCase);
        this.profileUseCase = profileUseCase;
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
