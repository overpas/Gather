package com.github.overpass.gather.commons.android.fragment

import androidx.fragment.app.Fragment
import com.github.overpass.gather.commons.android.DEFAULT_STRING

fun Fragment.getStringArg(key: String): String =
        arguments?.getString(key, DEFAULT_STRING) ?: DEFAULT_STRING