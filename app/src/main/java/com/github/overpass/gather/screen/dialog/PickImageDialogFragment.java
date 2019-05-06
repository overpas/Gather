package com.github.overpass.gather.screen.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class PickImageDialogFragment extends BaseDialogFragment {

    private static final String TAG = "PickImageDialogFragment";

    private OnClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnClickListener) {
            listener = (OnClickListener) context;
        } else {
            throw new RuntimeException(context + " must implement "
                    + OnClickListener.class.getSimpleName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Drawable gallery = getResources().getDrawable(R.drawable.ic_gallery, null);
        Drawable camera = getResources().getDrawable(R.drawable.ic_camera, null);
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.choose_image)
                .setPositiveButton(R.string.gallery, (dialog, which) -> {
                    listener.onGallery();
                })
                .setPositiveButtonIcon(gallery)
                .setNegativeButton(R.string.camera, (dialog, which) -> {
                    listener.onCamera();
                })
                .setNegativeButtonIcon(camera)
                .create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public static void show(@Nullable FragmentManager fragmentManager) {
        show(TAG, fragmentManager, true, PickImageDialogFragment::new);
    }

    public static void hide(@Nullable FragmentManager fragmentManager) {
        hide(TAG, fragmentManager, PickImageDialogFragment.class);
    }

    public interface OnClickListener {

        void onGallery();

        void onCamera();
    }
}
