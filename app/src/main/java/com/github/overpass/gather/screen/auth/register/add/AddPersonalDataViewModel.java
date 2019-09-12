package com.github.overpass.gather.screen.auth.register.add;

import android.app.Application;

import androidx.annotation.NonNull;

import com.github.overpass.gather.model.data.entity.splash.StartStatus;
import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.model.usecase.login.StartStatusUseCase;
import com.github.overpass.gather.screen.base.personal.DataViewModel;

import javax.inject.Inject;

public class AddPersonalDataViewModel extends DataViewModel {

    private final StartStatusUseCase startStatusUseCase;

    @Inject
    public AddPersonalDataViewModel(@NonNull Application application,
                                    StartStatusUseCase startStatusUseCase,
                                    ImageSourceUseCase imageSourceUseCase) {
        super(application);
        this.startStatusUseCase = startStatusUseCase;
        setImageSourceUseCase(imageSourceUseCase);
    }

    public void setSignUpComplete() {
        startStatusUseCase.setStartStatus(StartStatus.AUTHORIZED);
    }
}
