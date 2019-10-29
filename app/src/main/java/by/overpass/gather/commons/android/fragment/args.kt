package by.overpass.gather.commons.android.fragment

import androidx.fragment.app.Fragment
import by.overpass.gather.commons.android.DEFAULT_STRING

fun Fragment.getStringArg(key: String): String =
        arguments?.getString(key, DEFAULT_STRING) ?: DEFAULT_STRING