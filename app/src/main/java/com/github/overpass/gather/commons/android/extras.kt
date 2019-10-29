package com.github.overpass.gather.commons.android

import android.app.Activity

const val DEFAULT_STRING = "default_string"
const val DEFAULT_DOUBLE = 0.0

fun Activity.getStringExtra(key: String): String =
        intent?.getStringExtra(key) ?: DEFAULT_STRING

fun Activity.getDoubleExtra(key: String): Double =
        intent?.getDoubleExtra(key, DEFAULT_DOUBLE) ?: DEFAULT_DOUBLE