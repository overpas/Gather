package com.github.overpass.gather.screen.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class BaseDialogFragment extends DialogFragment {

    protected String getIdFromArgs(String key) {
        String defaultId = "-1";
        if (getArguments() != null) {
            String id = getArguments().getString(key, defaultId);
            if (id != null) {
                return id;
            }
        }
        return defaultId;
    }

    public static <F extends BaseDialogFragment> void show(String tag,
                                                           @Nullable FragmentManager manager,
                                                           boolean cancelable,
                                                           Producer<F> producer) {
        show(tag, manager, cancelable, null, producer);
    }

    public static <F extends BaseDialogFragment> void show(@NonNull String tag,
                                                           @Nullable FragmentManager manager,
                                                           boolean cancelable,
                                                           @Nullable Bundle arguments,
                                                           @NonNull Producer<F> producer) {
        if (manager == null) {
            Log.e(tag, "Passed FragmentManager is null");
            return;
        }
        Fragment previous = manager.findFragmentByTag(tag);
        if (previous != null) {
            manager.beginTransaction().remove(previous).commitNow();
        }
        F fragment = producer.produce();
        fragment.setCancelable(cancelable);
        fragment.setArguments(arguments);
        fragment.showNow(manager, tag);
    }

    public static <F extends BaseDialogFragment> void hide(String tag,
                                                           @Nullable FragmentManager manager,
                                                           Class<F> fragmentClass) {
        if (manager == null) {
            Log.e(tag, "Passed FragmentManager is null");
            return;
        }
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment != null && fragment.getClass().equals(fragmentClass)) {
            BaseDialogFragment dialogFragment = fragmentClass.cast(fragment);
            if (dialogFragment != null) {
                dialogFragment.dismiss();
            }
        }
    }

    public interface Producer<T> {
        T produce();
    }
}
