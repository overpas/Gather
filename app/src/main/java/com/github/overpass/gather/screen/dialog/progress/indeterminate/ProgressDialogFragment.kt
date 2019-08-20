package com.github.overpass.gather.screen.dialog.progress.indeterminate

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager

import com.github.overpass.gather.R
import com.github.overpass.gather.screen.base.BaseDialogFragment

class ProgressDialogFragment : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
                .setView(R.layout.fragment_dialog_progress)
                .setCancelable(false)
                .create()
    }

    companion object {

        private const val TAG = "ProgressDialogFragment"

        @JvmStatic
        fun show(fragmentManager: FragmentManager?) {
            show(TAG, fragmentManager, false) { ProgressDialogFragment() }
        }

        @JvmStatic
        fun hide(fragmentManager: FragmentManager?) {
            hide(TAG, fragmentManager, ProgressDialogFragment::class.java)
        }
    }
}
