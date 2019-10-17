package com.github.overpass.gather.screen.auth.register.signup

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import com.github.overpass.gather.App.Companion.appComponentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.di.register.signup.SignUpComponent
import com.github.overpass.gather.di.register.signup.SignUpComponentManager
import com.github.overpass.gather.model.commons.UIUtil.snackbar
import com.github.overpass.gather.model.commons.UIUtil.textOf
import com.github.overpass.gather.screen.auth.register.RegistrationFragment
import com.github.overpass.gather.screen.dialog.progress.indeterminate.ProgressDialogFragment
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : RegistrationFragment<SignUpViewModel, SignUpComponent>() {

    override val layoutRes: Int = R.layout.fragment_sign_up

    override val componentManager: SignUpComponentManager = appComponentManager.getSignUpComponentManager()

    override fun createComponent(): SignUpComponent = componentManager.getOrCreate(Unit)

    override fun onComponentCreated(component: SignUpComponent) {
        component.inject(this)
    }

    override fun createViewModel(): SignUpViewModel {
        return viewModelProvider.get(SignUpViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvSignUp.setOnClickListener {
            viewModel.signUp(textOf(tietEmail), textOf(tietPassword))
        }
    }

    override fun onBind() {
        super.onBind()
        viewModel.signUpError().observe(viewLifecycleOwner, Observer { handleError(it) })
        viewModel.signUpSuccess().observe(viewLifecycleOwner, Observer { handleSuccess() })
        viewModel.signUpProgress().observe(viewLifecycleOwner, Observer { handleProgress() })
        viewModel.invalidEmail().observe(viewLifecycleOwner, Observer { handleInvalidEmail(it) })
        viewModel.invalidPassword().observe(viewLifecycleOwner, Observer { handleInvalidPassword(it) })
    }

    private fun handleError(message: String) {
        snackbar(tietEmail, message)
    }

    private fun handleSuccess() {
        ProgressDialogFragment.hide(fragmentManager)
        snackbar(tietEmail, "Success")
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
