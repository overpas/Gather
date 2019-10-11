package com.github.overpass.gather.screen.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.lifecycle.Observer
import com.github.overpass.gather.App.Companion.appComponentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.screen.auth.login.LoginActivity
import com.github.overpass.gather.screen.auth.register.RegisterActivity
import com.github.overpass.gather.screen.base.BaseActivityKt
import com.github.overpass.gather.screen.map.MapActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : BaseActivityKt<SplashViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_splash_screen
    }

    override fun createViewModel(): SplashViewModel {
        return viewModelProvider.get(SplashViewModel::class.java)
    }

    override fun inject() {
        appComponentManager.getSplashComponent()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            playAnimation()
        }
    }

    override fun onBind() {
        super.onBind()
        viewModel.authorized().observe(this, Observer { handleAuthorized() })
        viewModel.unauthorized().observe(this, Observer { handleUnauthorized() })
        viewModel.notAddedData().observe(this, Observer { handleNotAddedData() })
        viewModel.unconfirmedEmail().observe(this, Observer { handleUnconfirmedEmail() })
    }

    override fun clearComponent() {
        super.clearComponent()
        appComponentManager.clearSplashComponent()
    }

    private fun playAnimation() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = LinearInterpolator()
        fadeIn.duration = 1300
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                Handler().postDelayed({ this@SplashScreenActivity.proceed() }, 700)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        ivLogo.startAnimation(fadeIn)
    }

    private fun proceed() {
        viewModel.onSplashAnimationComplete()
    }

    private fun handleUnconfirmedEmail() {
        RegisterActivity.start(this, 1)
    }

    private fun handleNotAddedData() {
        RegisterActivity.start(this, 2)
    }

    private fun handleUnauthorized() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun handleAuthorized() {
        startActivity(Intent(this, MapActivity::class.java))
        finish()
    }
}
