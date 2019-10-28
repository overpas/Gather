package com.github.overpass.gather.screen.dialog.delete;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.github.overpass.gather.App;
import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.Fragments;
import com.github.overpass.gather.screen.base.BaseDialogFragment;
import com.github.overpass.gather.screen.meeting.chat.DeleteStatus;

import org.jetbrains.annotations.NotNull;

import static com.github.overpass.gather.model.commons.AndroidParamsKt.getStringArg;
import static com.github.overpass.gather.model.commons.UIUtil.toast;

public class DeleteDialogFragment extends BaseDialogFragment<DeleteMessageViewModel> {

    private static final String TAG = "DeleteDialogFragment";
    private static final String MESSAGE_ID_KEY = "MESSAGE_ID_KEY";

    @Override
    protected void inject() {
        App.Companion.getComponentManager(this)
                .getDeleteMessageComponent()
                .inject(this);
    }

    @NotNull
    @Override
    protected DeleteMessageViewModel createViewModel() {
        return getViewModelProvider().get(DeleteMessageViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.should_delete)
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    viewModel.delete(getMessageId()).observe(
                            DeleteDialogFragment.this,
                            DeleteDialogFragment.this::handleDeletion
                    );
                })
                .setNegativeButton(R.string.cancel, ((dialog, id) -> {
                    dismiss();
                }))
                .create();
    }

    private void handleDeletion(DeleteStatus deleteStatus) {
        switch (deleteStatus.tag()) {
            case DeleteStatus.ERROR:
                handleDeletionError(deleteStatus.as(DeleteStatus.Error.class));
                break;
            case DeleteStatus.SUCCESS:
                handleDeletionSuccess();
                break;
        }
    }

    private void handleDeletionSuccess() {
        toast(this, getString(R.string.success));
        dismiss();
    }

    private void handleDeletionError(DeleteStatus.Error error) {
        toast(this, error.getThrowable().getLocalizedMessage());
        dismiss();
    }

    private String getMessageId() {
        return getStringArg(this, MESSAGE_ID_KEY);
    }

    public static void show(String messageId, FragmentManager fragmentManager) {
        Bundle args = new Bundle();
        args.putString(MESSAGE_ID_KEY, messageId);
        Fragments.Dialog.show(TAG, fragmentManager, true, args, DeleteDialogFragment::new);
    }
}
