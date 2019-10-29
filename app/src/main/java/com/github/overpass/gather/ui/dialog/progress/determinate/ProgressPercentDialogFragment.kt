package com.github.overpass.gather.ui.dialog.progress.determinate

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.commons.android.fragment.Fragments

class ProgressPercentDialogFragment : DialogFragment() {

    private lateinit var tvPercent: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_dialog_percent_progress, null)
                .also { tvPercent = it.findViewById(R.id.tvPercent) }
                .run { AlertDialog.Builder(context).setView(this) }
                .setCancelable(false)
                .create()
    }

    private fun setProgress(progress: Int) {
        val progressText = "$progress %"
        tvPercent.text = progressText
    }

    companion object {

        private const val TAG = "ProgressPercentDialogFragment"

        @JvmStatic
        fun show(fragmentManager: FragmentManager) {
            Fragments.Dialog.show(TAG, fragmentManager, false) { ProgressPercentDialogFragment() }
        }

        @JvmStatic
        fun hide(fragmentManager: FragmentManager) {
            Fragments.Dialog.hide(TAG, fragmentManager, ProgressPercentDialogFragment::class.java)
        }

        @JvmStatic
        fun progress(manager: FragmentManager, progress: Int) {
            val fragment = manager.findFragmentByTag(TAG)
            if (fragment != null && fragment is ProgressPercentDialogFragment) {
                fragment.setProgress(progress)
            } else {
                show(manager)
            }
        }
    }
}
