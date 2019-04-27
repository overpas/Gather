package com.github.overpass.gather.auth.login;

import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.auth.login.forgot.ForgotPasswordBottomFragment;
import com.github.overpass.gather.auth.register.RegisterActivity;
import com.github.overpass.gather.model.commons.base.BaseFragment;
import com.github.overpass.gather.dialog.ProgressDialogFragment;
import com.github.overpass.gather.map.MapActivity;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;
import static com.github.overpass.gather.model.commons.UIUtil.textOf;

public class SignInFragment extends BaseFragment<SignInViewModel> {

    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.tietPassword)
    TextInputEditText tietPassword;

    @Override
    protected SignInViewModel createViewModel() {
        return ViewModelProviders.of(this).get(SignInViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_sign_in;
    }

    public void handleSignInStatus(SignInStatus status) {
        switch (status.tag()) {
            case SignInStatus.ERROR:
                ProgressDialogFragment.hide(getFragmentManager());
                String message = status.as(SignInStatus.Error.class)
                        .getThrowable()
                        .getLocalizedMessage();
                snackbar(tietEmail, message);
                break;
            case SignInStatus.SUCCESS:
                ProgressDialogFragment.hide(getFragmentManager());
                startActivity(new Intent(getContext(), MapActivity.class));
                getActivity().finish();
                break;
            case SignInStatus.PROGRESS:
                ProgressDialogFragment.show(getFragmentManager());
                break;
            case SignInStatus.INVALID_EMAIL:
                ProgressDialogFragment.hide(getFragmentManager());
                snackbar(tietEmail, status.as(SignInStatus.InvalidEmail.class).getMessage());
                break;
            case SignInStatus.INVALID_PASSWORD:
                ProgressDialogFragment.hide(getFragmentManager());
                snackbar(tietEmail, status.as(SignInStatus.InvalidPassword.class).getMessage());
                break;
        }
    }

    @OnClick(R.id.tvSignIn)
    public void onSignInClick() {
        viewModel.signIn(textOf(tietEmail), textOf(tietPassword))
                .observe(getViewLifecycleOwner(), this::handleSignInStatus);
    }

    @OnClick(R.id.tvSignUp)
    public void onSignUpClicked() {
        startActivity(new Intent(getContext(), RegisterActivity.class));
    }

    @OnClick(R.id.tvForgotPassword)
    public void onForgotPasswordClick() {
        ForgotPasswordBottomFragment.open(getFragmentManager());
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }
}
