package com.github.overpass.gather.auth.register.signup;

import android.os.Handler;

import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.auth.register.RegistrationFragment;
import com.github.overpass.gather.dialog.ProgressDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.UIUtil.snackbar;
import static com.github.overpass.gather.UIUtil.textOf;

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
        viewModel.signUp2(textOf(tietEmail), textOf(tietPassword))
                .observe(getViewLifecycleOwner(), this::handleSignUp);
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }
}
