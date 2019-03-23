package com.github.overpass.gather.auth.register.signup;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.overpass.gather.ProgressDialogFragment;
import com.github.overpass.gather.R;
import com.github.overpass.gather.auth.register.RegistrationController;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.overpass.gather.UIUtil.snackbar;
import static com.github.overpass.gather.UIUtil.textOf;
import static com.github.overpass.gather.UIUtil.toast;

public class SignUpFragment extends Fragment {

    private SignUpViewModel viewModel;
    private RegistrationController registrationController;

    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.tietPassword)
    TextInputEditText tietPassword;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        subscribe();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registrationController = null;
    }

    private void subscribe() {
        viewModel.getSignUpData().observe(getViewLifecycleOwner(), this::handleSignUp);
    }

    public void handleSignUp(SignUpStatus status) {
        switch (status.tag()) {
            case SignUpStatus.ERROR:
                ProgressDialogFragment.hide(getFragmentManager());
                String message = status.as(SignUpStatus.Error.class)
                        .getThrowable()
                        .getLocalizedMessage();
                snackbar(tietEmail, message);
                break;
            case SignUpStatus.SUCCESS:
                ProgressDialogFragment.hide(getFragmentManager());
                snackbar(tietEmail, "Success");
                new Handler().postDelayed(() -> {
                    viewModel.moveToNextStep(registrationController);
                }, 200);
                break;
            case SignUpStatus.PROGRESS:
                ProgressDialogFragment.show(getFragmentManager());
                break;
            case SignUpStatus.INVALID_EMAIL:
                ProgressDialogFragment.hide(getFragmentManager());
                snackbar(tietEmail, status.as(SignUpStatus.InvalidEmail.class).getMessage());
                break;
            case SignUpStatus.INVALID_PASSWORD:
                ProgressDialogFragment.hide(getFragmentManager());
                snackbar(tietEmail, status.as(SignUpStatus.InvalidPassword.class).getMessage());
                break;
        }
    }

    @OnClick(R.id.tvSignUp)
    public void onSignUpClicked() {
        viewModel.signUp(textOf(tietEmail), textOf(tietPassword));
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }
}
