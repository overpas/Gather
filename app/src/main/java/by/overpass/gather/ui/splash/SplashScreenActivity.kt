package by.overpass.gather.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.lifecycle.on
import by.overpass.gather.ui.auth.login.LoginActivity
import by.overpass.gather.ui.auth.register.RegisterActivity
import by.overpass.gather.ui.base.BaseActivityKt
import by.overpass.gather.ui.map.MapActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : BaseActivityKt<SplashViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_splash_screen
    }

    override fun createViewModel(): SplashViewModel {
        return viewModelProvider.get(SplashViewModel::class.java)
    }

    override fun inject() {
        componentManager.getSplashComponent()
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
        on(viewModel.authorized()) {
            handleAuthorized()
        }
        on(viewModel.unauthorized()) {
            handleUnauthorized()
        }
        on(viewModel.notAddedData()) {
            handleNotAddedData()
        }
        on(viewModel.unconfirmedEmail()) {
            handleUnconfirmedEmail()
        }
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
