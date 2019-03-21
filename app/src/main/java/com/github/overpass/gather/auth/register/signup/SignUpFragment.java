package com.github.overpass.gather.auth.register.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.overpass.gather.R;
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

    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.tietPassword)
    TextInputEditText tietPassword;

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

    private void subscribe() {
        viewModel.getSignUpData().observe(getViewLifecycleOwner(), this::handleSignUp);
    }

    public void handleSignUp(SignUpStatus status) {
        switch (status.tag()) {
            case SignUpStatus.ERROR:
                String message = status.as(SignUpStatus.Error.class)
                        .getThrowable()
                        .getLocalizedMessage();
                snackbar(tietEmail, message);
                break;
            case SignUpStatus.SUCCESS:
                snackbar(tietEmail, "Success");
                break;
            case SignUpStatus.PROGRESS:
                toast(this, "Progress");
                break;
            case SignUpStatus.INVALID_EMAIL:
                snackbar(tietEmail, status.as(SignUpStatus.InvalidEmail.class).getMessage());
                break;
            case SignUpStatus.INVALID_PASSWORD:
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
