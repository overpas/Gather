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
                handleSuccess(confirmEmailStatus.as(ConfirmEmailStatus.Success.class));
                break;
            case ConfirmEmailStatus.ERROR:
                handleError(confirmEmailStatus.as(ConfirmEmailStatus.Error.class));
                break;
            case ConfirmEmailStatus.PROGRESS:
                handleProgress(confirmEmailStatus.as(ConfirmEmailStatus.Progress.class));
                break;
            default:
                handleFail(confirmEmailStatus.as(ConfirmEmailStatus.Fail.class));
        }
    }

    @OnClick(R.id.tvConfirm)
    public void onConfirmClicked() {
        viewModel.confirm().observe(getViewLifecycleOwner(), this::handleConfirmation);
    }

    private void handleSuccess(ConfirmEmailStatus.Success success) {
        ProgressDialogFragment.hide(getFragmentManager());
        viewModel.setEmailConfirmed();
        viewModel.moveToNextStep(registrationController);
    }

    private void handleError(ConfirmEmailStatus.Error error) {
        ProgressDialogFragment.hide(getFragmentManager());
        String message = error.getThrowable().getLocalizedMessage();
        toast(this, message);
    }

    private void handleProgress(ConfirmEmailStatus.Progress progress) {
        ProgressDialogFragment.show(getFragmentManager());
    }

    private void handleFail(ConfirmEmailStatus.Fail fail) {
        ProgressDialogFragment.hide(getFragmentManager());
        toast(this, fail.getMessage());
    }
}
