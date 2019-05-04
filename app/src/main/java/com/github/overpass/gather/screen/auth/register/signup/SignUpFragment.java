package com.github.overpass.gather.screen.auth.register.signup;

import android.os.Handler;

import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.auth.register.RegistrationFragment;
import com.github.overpass.gather.screen.dialog.ProgressDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;
import static com.github.overpass.gather.model.commons.UIUtil.textOf;

public class SignUpFragment extends RegistrationFragment<SignUpViewModel> {

    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.tietPassword)
    TextInputEditText tietPassword;

    @Override
    protected SignUpViewModel createViewModel() {
        return ViewModelProviders.of(this).get(SignUpViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_sign_up;
    }

    public void handleSignUp(SignUpStatus status) {
        switch (status.tag()) {
            case SignUpStatus.ERROR:
                handleError(status.as(SignUpStatus.Error.class));
                break;
            case SignUpStatus.SUCCESS:
                handleSuccess(status.as(SignUpStatus.Success.class));
                break;
            case SignUpStatus.PROGRESS:
                handleProgress(status.as(SignUpStatus.Progress.class));
                break;
            case SignUpStatus.INVALID_EMAIL:
                handleInvalidEmail(status.as(SignUpStatus.InvalidEmail.class));
                break;
            case SignUpStatus.INVALID_PASSWORD:
                handleInvalidPassword(status.as(SignUpStatus.InvalidPassword.class));
                break;
        }
    }

    @OnClick(R.id.tvSignUp)
    public void onSignUpClicked() {
        viewModel.signUp(textOf(tietEmail), textOf(tietPassword))
                .observe(getViewLifecycleOwner(), this::handleSignUp);
    }

    private void handleError(SignUpStatus.Error error) {
        ProgressDialogFragment.hide(getFragmentManager());
        String message = error.getThrowable().getLocalizedMessage();
        snackbar(tietEmail, message);
    }

    private void handleSuccess(SignUpStatus.Success success) {
        ProgressDialogFragment.hide(getFragmentManager());
        snackbar(tietEmail, "Success");
        viewModel.setSignUpInProgress();
        new Handler().postDelayed(() -> {
            viewModel.moveToNextStep(registrationController);
        }, 200);
    }

    private void handleProgress(SignUpStatus.Progress progress) {
        ProgressDialogFragment.show(getFragmentManager());
    }

    private void handleInvalidEmail(SignUpStatus.InvalidEmail invalidEmail) {
        ProgressDialogFragment.hide(getFragmentManager());
        tietEmail.setError(invalidEmail.getMessage());
    }

    private void handleInvalidPassword(SignUpStatus.InvalidPassword invalidPassword) {
        ProgressDialogFragment.hide(getFragmentManager());
        tietPassword.setError(invalidPassword.getMessage());
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }
}
