package com.github.overpass.gather.auth.login;

import android.content.Intent;

import com.github.overpass.gather.base.BaseFragment;
import com.github.overpass.gather.R;
import com.github.overpass.gather.auth.register.RegisterActivity;
import com.github.overpass.gather.map.MapActivity;
import com.google.android.material.textfield.TextInputEditText;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.UIUtil.snackbar;
import static com.github.overpass.gather.UIUtil.textOf;

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

    @Override
    protected void subscribe() {
        viewModel.getSignInData().observe(getViewLifecycleOwner(), this::handleSignInStatus);
    }

    public void handleSignInStatus(SignInStatus status) {
        switch (status.tag()) {
            case SignInStatus.ERROR:
                String message = status.as(SignInStatus.Error.class)
                        .getThrowable()
                        .getLocalizedMessage();
                snackbar(tietEmail, message);
                break;
            case SignInStatus.SUCCESS:
                startActivity(new Intent(getContext(), MapActivity.class));
                getActivity().finish();
                break;
            case SignInStatus.PROGRESS:
                snackbar(tietEmail, "Signing In ...");
                break;
            case SignInStatus.INVALID_EMAIL:
                snackbar(tietEmail, status.as(SignInStatus.InvalidEmail.class).getMessage());
                break;
            case SignInStatus.INVALID_PASSWORD:
                snackbar(tietEmail, status.as(SignInStatus.InvalidPassword.class).getMessage());
                break;
        }
    }

    @OnClick(R.id.tvSignIn)
    public void onSignInClick() {
        viewModel.signIn(textOf(tietEmail), textOf(tietPassword));
    }

    @OnClick(R.id.tvSignUp)
    public void onSignUpClicked() {
        startActivity(new Intent(getContext(), RegisterActivity.class));
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }
}
