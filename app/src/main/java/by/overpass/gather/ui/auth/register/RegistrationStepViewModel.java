package by.overpass.gather.ui.auth.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class RegistrationStepViewModel extends AndroidViewModel {

    public RegistrationStepViewModel(@NonNull Application application) {
        super(application);
    }

    public void moveToNextStep(RegistrationController registrationController) {
        registrationController.moveToNextStep();
    }
}
