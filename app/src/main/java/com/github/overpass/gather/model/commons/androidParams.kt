package com.github.overpass.gather.model.commons

import android.app.Activity
import androidx.fragment.app.Fragment

private const val DEFAULT_STRING = "default_string"
private const val DEFAULT_DOUBLE = 0.0

fun Fragment.getStringArg(key: String): String =
        arguments?.getString(key, DEFAULT_STRING) ?: DEFAULT_STRING

fun Activity.getStringExtra(key: String): String =
        intent?.getStringExtra(key) ?: DEFAULT_STRING

fun Activity.getDoubleExtra(key: String): Double =
        intent?.getDoubleExtra(key, DEFAULT_DOUBLE) ?: DEFAULT_DOUBLE