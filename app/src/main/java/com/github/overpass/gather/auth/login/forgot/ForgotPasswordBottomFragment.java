package com.github.overpass.gather.auth.login.forgot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.dialog.ProgressDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.overpass.gather.UIUtil.snackbar;
import static com.github.overpass.gather.UIUtil.textOf;
import static com.github.overpass.gather.UIUtil.toast;

public class ForgotPasswordBottomFragment extends BottomSheetDialogFragment {

    private static final String TAG = "ForgotPasswordBottomFra";

    private ForgotPasswordViewModel viewModel;

    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_forgot_password, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);
    }

    @OnClick(R.id.tvSendForgotPassword)
    public void onSendForgotPassword() {
        viewModel.sendForgotPassword(textOf(tietEmail))
                .observe(getViewLifecycleOwner(), this::handleForgotStatus);
    }

    private void handleForgotStatus(ForgotStatus forgotStatus) {
        switch (forgotStatus.tag()) {
            case ForgotStatus.ERROR:
                ProgressDialogFragment.hide(getFragmentManager());
                snackbar(tietEmail, forgotStatus.as(ForgotStatus.Error.class)
                        .getThrowable()
                        .getLocalizedMessage());
                break;
            case ForgotStatus.SUCCESS:
                ProgressDialogFragment.hide(getFragmentManager());
                toast(this, "Success!");
                dismiss();
                break;
            case ForgotStatus.INVALID_EMAIL:
                ProgressDialogFragment.hide(getFragmentManager());
                tietEmail.setError(getString(R.string.invalid_email));
                break;
            case ForgotStatus.PROGRESS:
                ProgressDialogFragment.show(getFragmentManager());
                break;
        }
    }

    public static void open(FragmentManager fragmentManager) {
        ForgotPasswordBottomFragment fragment = new ForgotPasswordBottomFragment();
        fragment.show(fragmentManager, TAG);
    }
}
