package com.github.overpass.gather.screen.auth.register.add;

import android.app.Application;

import androidx.annotation.NonNull;

import com.github.overpass.gather.model.repo.pref.PreferenceRepo;
import com.github.overpass.gather.model.repo.register.SignUpRepo;
import com.github.overpass.gather.model.usecase.login.StartStatusUseCase;
import com.github.overpass.gather.screen.base.personal.DataViewModel;
import com.github.overpass.gather.screen.splash.StartStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddPersonalDataViewModel extends DataViewModel {

    private final StartStatusUseCase startStatusUseCase;

    public AddPersonalDataViewModel(@NonNull Application application) {
        super(application);
        startStatusUseCase = new StartStatusUseCase(
                new PreferenceRepo(application),
                new SignUpRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
        );
    }

    public void setSignUpComplete() {
        startStatusUseCase.setStartStatus(StartStatus.AUTHORIZED);
    }
}
