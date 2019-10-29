package com.github.overpass.gather.ui.auth.register.confirm

import android.os.Bundle
import android.view.View
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.commons.android.lifecycle.on
import com.github.overpass.gather.commons.android.toast
import com.github.overpass.gather.ui.auth.register.RegistrationFragment
import com.github.overpass.gather.ui.dialog.progress.indeterminate.ProgressDialogFragment
import kotlinx.android.synthetic.main.fragment_confirm_email.*

class ConfirmEmailFragment : RegistrationFragment<ConfirmEmailViewModel>() {

    override fun getLayoutRes(): Int = R.layout.fragment_confirm_email

    override fun createViewModel(): ConfirmEmailViewModel {
        return viewModelProvider.get(ConfirmEmailViewModel::class.java)
    }

    override fun inject() {
        componentManager.getConfirmationComponent()
                .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvConfirm.setOnClickListener {
            viewModel.confirm()
        }
    }

    override fun onBind() {
        super.onBind()
        on(viewModel.confirmationError()) {
            handleError(it)
        }
        on(viewModel.confirmationSuccess()) {
            handleSuccess()
        }
        on(viewModel.confirmationProgress()) {
            handleProgress()
        }
    }

    private fun handleSuccess() {
        ProgressDialogFragment.hide(fragmentManager)
        viewModel.setEmailConfirmed()
        registrationController!!.moveToNextStep()
    }

    private fun handleError(message: String) {
        toast(message)
    }

    private fun handleProgress() {
        ProgressDialogFragment.show(fragmentManager)
    }

    companion object {

        @JvmStatic
        fun newInstance(): ConfirmEmailFragment {
            return ConfirmEmailFragment()
        }
    }
}
