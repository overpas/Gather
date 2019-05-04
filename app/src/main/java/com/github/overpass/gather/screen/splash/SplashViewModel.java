package com.github.overpass.gather.screen.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.model.commons.Runners;
import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.pref.PreferenceRepo;
import com.github.overpass.gather.model.repo.register.SignUpRepo;
import com.github.overpass.gather.model.usecase.register.SignUpUseCase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashViewModel extends AndroidViewModel {

    private final SignUpUseCase signUpUseCase;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        signUpUseCase = new SignUpUseCase(
                new SignUpRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()),
                new BaseValidator(),
                new PreferenceRepo(application)
        );
    }

    public LiveData<StartStatus> onSplashAnimationComplete() {
        MutableLiveData<StartStatus> startStatusData = new MutableLiveData<>();
        Runners.io().execute(() -> {
            StartStatus startStatus = signUpUseCase.getStartStatus();
            startStatusData.postValue(startStatus);
        });
        return startStatusData;
    }
}
