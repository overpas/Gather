package by.overpass.gather.ui.auth.register.signup

import android.os.Bundle
import android.os.Handler
import android.view.View
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.lifecycle.on
import by.overpass.gather.commons.android.snackbar
import by.overpass.gather.commons.android.text
import by.overpass.gather.ui.auth.register.RegistrationFragment
import by.overpass.gather.ui.dialog.progress.indeterminate.ProgressDialogFragment
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SignUpFragment : RegistrationFragment<SignUpViewModel>() {

    override fun getLayoutRes(): Int = R.layout.fragment_sign_up

    override fun createViewModel(): SignUpViewModel {
        return viewModelProvider.get(SignUpViewModel::class.java)
    }

    override fun inject() {
        componentManager.getSignUpComponent()
                .inject(this)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvSignUp.setOnClickListener {
            viewModel.signUp(tietEmail.text(), tietPassword.text())
        }
    }

    override fun onBind() {
        super.onBind()
        on(viewModel.signUpError()) {
            handleError(it)
        }
        on(viewModel.signUpSuccess()) {
            handleSuccess()
        }
        on(viewModel.signUpProgress()) {
            handleProgress()
        }
        on(viewModel.invalidEmail()) {
            handleInvalidEmail(it)
        }
        on(viewModel.invalidPassword()) {
            handleInvalidPassword(it)
        }
    }

    private fun handleError(message: String) {
        tietEmail.snackbar(message)
    }

    private fun handleSuccess() {
        ProgressDialogFragment.hide(fragmentManager)
        tietEmail.snackbar("Success")
        viewModel.setSignUpInProgress()
        Handler().postDelayed({
            registrationController!!.moveToNextStep()
        }, 200)
    }

    private fun handleProgress() {
        ProgressDialogFragment.show(fragmentManager)
    }

    private fun handleInvalidEmail(message: String) {
        ProgressDialogFragment.hide(fragmentManager)
        tietEmail.error = message
    }

    private fun handleInvalidPassword(message: String) {
        ProgressDialogFragment.hide(fragmentManager)
        tietPassword.error = message
    }

    companion object {

        @JvmStatic
        fun newInstance(): SignUpFragment {
            return SignUpFragment()
        }
    }
}
