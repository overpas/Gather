package by.overpass.gather.ui.auth.login.forgot

import androidx.fragment.app.FragmentManager
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.lifecycle.on
import by.overpass.gather.commons.android.snackbar
import by.overpass.gather.commons.android.text
import by.overpass.gather.commons.android.toast
import by.overpass.gather.ui.base.BaseBottomSheetDialogFragment
import by.overpass.gather.ui.dialog.progress.indeterminate.ProgressDialogFragment
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
        tietEmail.setOnClickListener {
            viewModel.sendForgotPassword(tietEmail.text())
        }
        on(viewModel.invalidEmail()) {
            handleInvalidEmail()
        }
        on(viewModel.resetPasswordError()) {
            handleError(it)
        }
        on(viewModel.resetPasswordProgress()) {
            handleProgress()
        }
        on(viewModel.resetPasswordSuccess()) {
            handleSuccess()
        }
    }

    private fun handleError(message: String) {
        ProgressDialogFragment.hide(fragmentManager)
        tietEmail.snackbar(message)
    }

    private fun handleProgress() {
        ProgressDialogFragment.show(fragmentManager)
    }

    private fun handleSuccess() {
        ProgressDialogFragment.hide(fragmentManager)
        toast( "Success!")
        dismiss()
    }

    private fun handleInvalidEmail() {
        ProgressDialogFragment.hide(fragmentManager)
        tietEmail.error = getString(R.string.invalid_email)
    }

    companion object {

        private const val TAG = "ForgotPasswordBottomFra"

        @JvmStatic
        fun open(fragmentManager: FragmentManager) {
            val fragment = ForgotPasswordBottomFragment()
            fragment.show(fragmentManager, TAG)
        }
    }
}
