package com.github.overpass.gather.screen.dialog.delete;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.base.BaseDialogFragment;
import com.github.overpass.gather.screen.meeting.chat.DeleteStatus;

import static com.github.overpass.gather.model.commons.UIUtil.toast;

public class DeleteDialogFragment extends BaseDialogFragment {

    private static final String TAG = "DeleteDialogFragment";
    private static final String MESSAGE_ID_KEY = "MESSAGE_ID_KEY";
    private static final String MEETING_ID_KEY = "MEETING_ID_KEY";

    private DeleteMessageViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(DeleteMessageViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.should_delete)
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    viewModel.delete(getMeetingId(), getMessageId()).observe(
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
                handleDeletionSuccess(deleteStatus.as(DeleteStatus.Success.class));
                break;
        }
    }

    private void handleDeletionSuccess(DeleteStatus.Success success) {
        toast(this, getString(R.string.success));
        dismiss();
    }

    private void handleDeletionError(DeleteStatus.Error error) {
        toast(this, error.getThrowable().getLocalizedMessage());
        dismiss();
    }

    private String getMessageId() {
        return getIdFromArgs(MESSAGE_ID_KEY);
    }

    private String getMeetingId() {
        return getIdFromArgs(MEETING_ID_KEY);
    }

    public static void show(String meetingId, String messageId, FragmentManager fragmentManager) {
        Bundle args = new Bundle();
        args.putString(MEETING_ID_KEY, meetingId);
        args.putString(MESSAGE_ID_KEY, messageId);
        show(TAG, fragmentManager, true, args, DeleteDialogFragment::new);
    }

    public static void hide(FragmentManager fragmentManager) {

    }
}
