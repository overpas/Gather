package com.github.overpass.gather.screen.base

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.replace(
        fragment: Fragment,
        tag: String,
        @IdRes containerId: Int,
        addToBackStack: Boolean
) = supportFragmentManager
        .beginTransaction()
        .replace(containerId, fragment, tag)
        .apply { if (addToBackStack) addToBackStack(null) }
        .commit()