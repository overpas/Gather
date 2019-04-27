package com.github.overpass.gather.screen.auth.register.confirm;

import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.auth.register.RegistrationFragment;
import com.github.overpass.gather.screen.dialog.ProgressDialogFragment;

import butterknife.OnClick;

import static com.github.overpass.gather.model.commons.UIUtil.toast;

public class ConfirmEmailFragment extends RegistrationFragment<ConfirmEmailViewModel> {

    public static ConfirmEmailFragment newInstance() {
        return new ConfirmEmailFragment();
    }

    @Override
    protected ConfirmEmailViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ConfirmEmailViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_confirm_email;
    }

    private void handleConfirmation(ConfirmEmailStatus confirmEmailStatus) {
        switch (confirmEmailStatus.tag()) {
            case ConfirmEmailStatus.SUCCESS:
                ProgressDialogFragment.hide(getFragmentManager());
                viewModel.moveToNextStep(registrationController);
                break;
            case ConfirmEmailStatus.ERROR:
                ProgressDialogFragment.hide(getFragmentManager());
                String message = confirmEmailStatus.as(ConfirmEmailStatus.Error.class)
                        .getThrowable()
                        .getLocalizedMessage();
                toast(this, message);
                break;
            case ConfirmEmailStatus.PROGRESS:
                ProgressDialogFragment.show(getFragmentManager());
                break;
            default:
                ProgressDialogFragment.hide(getFragmentManager());
                toast(this,
                        confirmEmailStatus.as(ConfirmEmailStatus.Fail.class).getMessage());
        }
    }

    @OnClick(R.id.tvConfirm)
    public void onConfirmClicked() {
        viewModel.confirm().observe(getViewLifecycleOwner(), this::handleConfirmation);
    }
}
