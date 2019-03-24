package com.github.overpass.gather.auth.register;

import android.content.Context;

import com.github.overpass.gather.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public abstract class RegistrationFragment<VM extends ViewModel> extends BaseFragment<VM> {

    protected RegistrationController registrationController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationController) {
            registrationController = (RegistrationController) context;
        } else {
            throw new RuntimeException(context + " must implement "
                    + RegistrationController.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registrationController = null;
    }
}
