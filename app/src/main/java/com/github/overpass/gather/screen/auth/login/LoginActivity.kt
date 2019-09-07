package com.github.overpass.gather.screen.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.github.overpass.gather.App.Companion.appComponent
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.UIUtil.snackbar
import com.github.overpass.gather.model.commons.UIUtil.textOf
import com.github.overpass.gather.screen.auth.login.forgot.ForgotPasswordBottomFragment
import com.github.overpass.gather.screen.auth.register.RegisterActivity
import com.github.overpass.gather.screen.base.BaseActivityKt
import com.github.overpass.gather.screen.dialog.progress.indeterminate.ProgressDialogFragment
import com.github.overpass.gather.screen.map.MapActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

class LoginActivity : BaseActivityKt<SignInViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_sign_in
    }

    override fun createViewModel(): SignInViewModel {
        return viewModelProvider.get(SignInViewModel::class.java)
    }

    override fun onInject() {
        appComponent.signIn()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvSignUp.setOnClickListener {
            RegisterActivity.start(this, 0)
        }
        tvSignIn.setOnClickListener {
            viewModel.signIn(textOf(tietEmail), textOf(tietPassword))
        }
        tvForgotPassword.setOnClickListener {
            ForgotPasswordBottomFragment.open(supportFragmentManager)
        }
    }

    override fun onBind() {
        super.onBind()
        viewModel.signInError().observe(this, Observer { this.handleSignInError(it) })
        viewModel.signInSuccess().observe(this, Observer { this.handleSignInSuccess() })
        viewModel.signInProgress().observe(this, Observer { this.handleSignInProgress() })
        viewModel.invalidEmail().observe(this, Observer { this.handleInvalidEmail(it) })
        viewModel.invalidPassword().observe(this, Observer { this.handleInvalidPassword(it) })
    }

    private fun handleInvalidPassword(message: String) {
        ProgressDialogFragment.hide(supportFragmentManager)
        snackbar(tietEmail, message)
    }

    private fun handleInvalidEmail(message: String) {
        ProgressDialogFragment.hide(supportFragmentManager)
        snackbar(tietEmail, message)
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
        snackbar(tietEmail, message)
    }
}
