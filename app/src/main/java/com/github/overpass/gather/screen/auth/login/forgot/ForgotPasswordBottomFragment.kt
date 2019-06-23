package com.github.overpass.gather.screen.auth.login.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.github.overpass.gather.R
import com.github.overpass.gather.model.data.entity.forgot.ForgotStatus
import com.github.overpass.gather.screen.dialog.ProgressDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

import com.github.overpass.gather.model.commons.UIUtil.snackbar
import com.github.overpass.gather.model.commons.UIUtil.textOf
import com.github.overpass.gather.model.commons.UIUtil.toast
import kotlinx.android.synthetic.main.fragment_bottom_forgot_password.*

class ForgotPasswordBottomFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_forgot_password, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java)
        tietEmail.setOnClickListener {
            viewModel.sendForgotPassword(textOf(tietEmail))
        }
        viewModel.invalidEmail().observe(viewLifecycleOwner, Observer { handleInvalidEmail() })
        viewModel.resetPasswordError().observe(viewLifecycleOwner, Observer { handleError(it) })
        viewModel.resetPasswordProgress().observe(viewLifecycleOwner, Observer { handleProgress() })
        viewModel.resetPasswordSuccess().observe(viewLifecycleOwner, Observer { handleSuccess() })
    }

    private fun handleError(message: String) {
        ProgressDialogFragment.hide(fragmentManager)
        snackbar(tietEmail, message)
    }

    private fun handleProgress() {
        ProgressDialogFragment.show(fragmentManager)
    }

    private fun handleSuccess() {
        ProgressDialogFragment.hide(fragmentManager)
        toast(this, "Success!")
        dismiss()
    }

    private fun handleInvalidEmail() {
        ProgressDialogFragment.hide(fragmentManager)
        tietEmail.error = getString(R.string.invalid_email)
    }

    companion object {

        private val TAG = "ForgotPasswordBottomFra"

        @JvmStatic
        fun open(fragmentManager: FragmentManager) {
            val fragment = ForgotPasswordBottomFragment()
            fragment.show(fragmentManager, TAG)
        }
    }
}
