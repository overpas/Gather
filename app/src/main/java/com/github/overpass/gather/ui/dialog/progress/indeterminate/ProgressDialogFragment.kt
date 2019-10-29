package com.github.overpass.gather.ui.dialog.progress.indeterminate

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

import com.github.overpass.gather.R
import com.github.overpass.gather.commons.android.fragment.Fragments

class ProgressDialogFragment : DialogFragment() {

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
            Fragments.Dialog.show(TAG, fragmentManager, false) { ProgressDialogFragment() }
        }

        @JvmStatic
        fun hide(fragmentManager: FragmentManager?) {
            Fragments.Dialog.hide(TAG, fragmentManager, ProgressDialogFragment::class.java)
        }
    }
}
