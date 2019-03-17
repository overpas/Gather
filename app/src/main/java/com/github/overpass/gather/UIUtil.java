package com.github.overpass.gather;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class UIUtil {

    public static String textOf(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static void toast(Fragment fragment, String message) {
        Toast.makeText(fragment.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}