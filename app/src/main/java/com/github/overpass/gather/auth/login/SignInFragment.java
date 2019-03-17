package com.github.overpass.gather.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.overpass.gather.R;
import com.github.overpass.gather.map.MapActivity;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.overpass.gather.UIUtil.textOf;
import static com.github.overpass.gather.UIUtil.toast;

public class SignInFragment extends Fragment {

    private SignInViewModel viewModel;

    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.tietPassword)
    TextInputEditText tietPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        subscribe();
    }

    private void subscribe() {
        viewModel.getSignInData().observe(getViewLifecycleOwner(), this::handleSignInStatus);
    }

    public void handleSignInStatus(SignInStatus status) {
        switch (status.tag()) {
            case SignInStatus.ERROR:
                String message = status.as(SignInStatus.Error.class)
                        .getThrowable()
                        .getLocalizedMessage();
                toast(this, message);
                break;
            case SignInStatus.SUCCESS:
                startActivity(new Intent(getContext(), MapActivity.class));
                getActivity().finish();
                break;
            case SignInStatus.PROGRESS:
                toast(this, "Signing In ...");
                break;
            case SignInStatus.INVALID_EMAIL:
                toast(this, status.as(SignInStatus.InvalidEmail.class).getMessage());
                break;
            case SignInStatus.INVALID_PASSWORD:
                toast(this, status.as(SignInStatus.InvalidPassword.class).getMessage());
                break;
        }
    }

    @OnClick(R.id.tvSignIn)
    public void onSignInClick() {
        viewModel.signIn(textOf(tietEmail), textOf(tietPassword));
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }
}
