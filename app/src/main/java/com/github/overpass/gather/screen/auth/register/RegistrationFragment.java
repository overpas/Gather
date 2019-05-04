package com.github.overpass.gather.screen.auth.register;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.screen.base.BaseFragment;

public abstract class RegistrationFragment<VM extends ViewModel> extends BaseFragment<VM> {

    private static final String TAG = "RegistrationFragment";

    protected RegistrationController registrationController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationController) {
            registrationController = (RegistrationController) context;
        } else {
            Log.d(TAG, context + " must implement "
                    + RegistrationController.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registrationController = null;
    }

    protected int getInitialStep() {
        int initialStep = 0;
        if (registrationController != null) {
            initialStep = registrationController.getInitialStep();
        }
        return initialStep;
    }
}
