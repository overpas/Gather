package by.overpass.gather.model.usecase.userdata;

import androidx.core.util.Consumer;

import by.overpass.gather.model.data.entity.splash.StartStatus;
import by.overpass.gather.model.repo.login.AuthRepo;
import by.overpass.gather.model.repo.pref.PreferenceRepo;
import by.overpass.gather.model.repo.user.UserDataRepo;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import by.overpass.gather.model.data.entity.splash.StartStatus;

public class ProfileUseCase {

    private final UserDataRepo userDataRepo;
    private final AuthRepo authRepo;
    private final PreferenceRepo preferenceRepo;

    @Inject
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
        authRepo.signOut();
        preferenceRepo.setStartStatus(StartStatus.UNAUTHORIZED);
        onSuccess.run();
    }
}
