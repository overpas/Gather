package com.github.overpass.gather.screen.auth.login.forgot

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.UIUtil.*
import com.github.overpass.gather.screen.base.BaseBottomSheetDialogFragment
import com.github.overpass.gather.screen.dialog.progress.indeterminate.ProgressDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_forgot_password.*

class ForgotPasswordBottomFragment : BaseBottomSheetDialogFragment<ForgotPasswordViewModel>() {

    override fun getLayoutRes(): Int = R.layout.fragment_bottom_forgot_password

    override fun inject() {
        componentManager.getForgotComponent()
                .inject(this)
    }

    override fun createViewModel(): ForgotPasswordViewModel =
            viewModelProvider.get(ForgotPasswordViewModel::class.java)

    override fun onBind() {
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
