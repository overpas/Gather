package com.github.overpass.gather.model.commons

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

object Fragments {

    object Dialog {

        @JvmStatic
        fun <F : DialogFragment> show(tag: String,
                                      manager: FragmentManager?,
                                      cancelable: Boolean,
                                      producer: () -> F) {
            show(tag, manager, cancelable, null, producer)
        }

        @JvmStatic
        fun <F : DialogFragment> show(tag: String,
                                      manager: FragmentManager?,
                                      cancelable: Boolean,
                                      arguments: Bundle?,
                                      producer: () -> F) {
            requireNotNull(manager) { "Passed FragmentManager is null" }
            val previous = manager.findFragmentByTag(tag)
            if (previous != null) {
                manager.beginTransaction().remove(previous).commitNow()
            }
            val fragment = producer()
            fragment.isCancelable = cancelable
            fragment.arguments = arguments
            fragment.showNow(manager, tag)
        }

        @JvmStatic
        fun <F : DialogFragment> hide(tag: String,
                                      manager: FragmentManager?,
                                      fragmentClass: Class<F>) {
            requireNotNull(manager) { "Passed FragmentManager is null" }
            val fragment = manager.findFragmentByTag(tag)
            if (fragment != null && fragment.javaClass == fragmentClass) {
                val dialogFragment = fragmentClass.cast(fragment)
                dialogFragment?.dismiss()
            }
        }
    }
}