package com.github.overpass.gather.screen.base

import androidx.lifecycle.ViewModel

abstract class BackPressActivity<VM : ViewModel, C> : BaseActivity<VM, C>() {

    override fun onBackPressed() {
        if (!supportFragmentManager.fragments
                        .filterIsInstance<BackPressFragment>()
                        .any { it.handleBackPress() }) {
            super.onBackPressed()
        }
    }
}
