package com.github.overpass.gather.ui.auth.login

import android.content.Intent
import android.os.Bundle
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.commons.android.lifecycle.on
import com.github.overpass.gather.commons.android.snackbar
import com.github.overpass.gather.commons.android.text
import com.github.overpass.gather.ui.auth.login.forgot.ForgotPasswordBottomFragment
import com.github.overpass.gather.ui.auth.register.RegisterActivity
import com.github.overpass.gather.ui.base.BaseActivityKt
import com.github.overpass.gather.ui.dialog.progress.indeterminate.ProgressDialogFragment
import com.github.overpass.gather.ui.map.MapActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

class LoginActivity : BaseActivityKt<SignInViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_sign_in
    }

    override fun createViewModel(): SignInViewModel {
        return viewModelProvider.get(SignInViewModel::class.java)
    }

    override fun inject() {
        componentManager.getSignInComponent()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvSignUp.setOnClickListener {
            RegisterActivity.start(this, 0)
        }
        tvSignIn.setOnClickListener {
            viewModel.signIn(tietEmail.text(), tietPassword.text())
        }
        tvForgotPassword.setOnClickListener {
            ForgotPasswordBottomFragment.open(supportFragmentManager)
        }
    }

    override fun onBind() {
        super.onBind()
        on(viewModel.signInError()) {
            handleSignInError(it)
        }
        on(viewModel.signInSuccess()) {
            handleSignInSuccess()
        }
        on(viewModel.signInProgress()) {
            handleSignInProgress()
        }
        on(viewModel.invalidEmail()) {
            handleInvalidEmail(it)
        }
        on(viewModel.invalidPassword()) {
            handleInvalidPassword(it)
        }
    }

    private fun handleInvalidPassword(message: String) {
        ProgressDialogFragment.hide(supportFragmentManager)
        tietEmail.snackbar(message)
    }

    private fun handleInvalidEmail(message: String) {
        ProgressDialogFragment.hide(supportFragmentManager)
        tietEmail.snackbar(message)
    }

    private fun handleSignInProgress() {
        ProgressDialogFragment.show(supportFragmentManager)
    }

    private fun handleSignInSuccess() {
        ProgressDialogFragment.hide(supportFragmentManager)
        startActivity(Intent(this, MapActivity::class.java))
        finish()
    }

    private fun handleSignInError(message: String) {
        ProgressDialogFragment.hide(supportFragmentManager)
        tietEmail.snackbar(message)
    }
}
