package com.github.overpass.gather;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ProgressDialogFragment extends DialogFragment {

    private static final String TAG = "ProgressDialogFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setView(R.layout.fragment_dialog_progress)
                .setCancelable(false)
                .create();
    }

    public static void show(FragmentManager fragmentManager) {
        Fragment previous = fragmentManager.findFragmentByTag(TAG);
        if (previous != null) {
            fragmentManager.beginTransaction().remove(previous).commitNow();
        }
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setCancelable(false);
        fragment.showNow(fragmentManager, TAG);
    }

    public static void hide(FragmentManager fragmentManager) {
        Fragment progressFragment = fragmentManager.findFragmentByTag(TAG);
        if (progressFragment instanceof ProgressDialogFragment) {
            ((ProgressDialogFragment) progressFragment).dismiss();
        }
    }
}
