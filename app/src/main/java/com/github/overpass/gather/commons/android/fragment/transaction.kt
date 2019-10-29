package com.github.overpass.gather.commons.android.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.transaction() = supportFragmentManager.beginTransaction()

fun Fragment.transaction() = requireFragmentManager().beginTransaction()

fun Fragment.childTransaction() = childFragmentManager.beginTransaction()

fun FragmentTransaction.addToBackStack(
        shouldAdd: Boolean,
        tag: String? = null
): FragmentTransaction = this
        .takeIf { shouldAdd }
        ?.addToBackStack(tag)
        ?: this

