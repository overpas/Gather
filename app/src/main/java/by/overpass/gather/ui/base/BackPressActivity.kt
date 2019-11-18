package by.overpass.gather.ui.base

import androidx.lifecycle.ViewModel

abstract class BackPressActivity<VM : ViewModel> : BaseActivityKt<VM>() {

    override fun onBackPressed() {
        if (!supportFragmentManager.fragments
                        .filterIsInstance<BackPressFragment>()
                        .any { it.handleBackPress() }) {
            super.onBackPressed()
        }
    }
}
