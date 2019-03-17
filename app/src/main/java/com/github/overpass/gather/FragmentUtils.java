package com.github.overpass.gather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {

    public static void replace(FragmentManager fragmentManager,
                               int containerId,
                               Fragment fragment,
                               boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(containerId, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
