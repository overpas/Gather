package com.github.overpass.gather.screen.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.base.BaseDialogFragment;

public class ProgressDialogFragment extends BaseDialogFragment {

    private static final String TAG = "ProgressDialogFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setView(R.layout.fragment_dialog_progress)
                .setCancelable(false)
                .create();
    }

    public static void show(@Nullable FragmentManager fragmentManager) {
        show(TAG, fragmentManager, false, ProgressDialogFragment::new);
    }

    public static void hide(@Nullable FragmentManager fragmentManager) {
        hide(TAG, fragmentManager, ProgressDialogFragment.class);
    }
}
