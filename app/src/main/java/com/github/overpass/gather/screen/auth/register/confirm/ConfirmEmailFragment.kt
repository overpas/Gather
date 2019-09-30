package com.github.overpass.gather.screen.auth.register.confirm

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.UIUtil.toast
import com.github.overpass.gather.screen.auth.register.RegistrationFragment
import com.github.overpass.gather.screen.dialog.progress.indeterminate.ProgressDialogFragment
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
        viewModel.confirmationError().observe(viewLifecycleOwner, Observer { handleError(it) })
        viewModel.confirmationSuccess().observe(viewLifecycleOwner, Observer { handleSuccess() })
        viewModel.confirmationProgress().observe(viewLifecycleOwner, Observer { handleProgress() })
    }

    override fun clearComponent() {
        super.clearComponent()
        componentManager.clearConfirmationComponent()
    }

    private fun handleSuccess() {
        ProgressDialogFragment.hide(fragmentManager)
        viewModel.setEmailConfirmed()
        registrationController!!.moveToNextStep()
    }

    private fun handleError(message: String) {
        toast(this, message)
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
