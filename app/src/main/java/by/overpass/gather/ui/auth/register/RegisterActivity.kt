package by.overpass.gather.ui.auth.register

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.lifecycle.on
import by.overpass.gather.ui.base.BaseActivityKt
import by.overpass.gather.ui.dialog.PickImageDialogFragment
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivityKt<RegisterViewModel>(), RegistrationController, PickImageDialogFragment.OnClickListener {

    override fun getLayoutRes(): Int {
        return R.layout.activity_register
    }

    override fun createViewModel(): RegisterViewModel {
        return viewModelProvider.get(RegisterViewModel::class.java)
    }

    @SuppressLint("UseValueOf")
    override fun inject() {
        componentManager.getRegisterComponentFactory(lifecycle)
                .create(Integer(getStep()))
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setViewFlipperAnimations()
    }

    override fun onBind() {
        on(viewModel.getRegistrationProgressData()) {
            moveToNextStep(it)
        }
    }

    override fun moveToNextStep() {
        viewModel.next()
    }

    override fun onGallery() {
        viewModel.onGallery()
    }

    override fun onCamera() {
        viewModel.onCamera()
    }

    override fun getInitialStep(): Int {
        return getStep()
    }

    private fun moveToNextStep(step: Int) {
        if (viewModel.shouldShowNextStep(step)) {
            viewFlipper.displayedChild = step
        }
    }

    private fun setViewFlipperAnimations() {
        val imgAnimationIn = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_right)
        viewFlipper.inAnimation = imgAnimationIn
        val imgAnimationOut = AnimationUtils.loadAnimation(this,
                R.anim.slide_out_left)
        viewFlipper.outAnimation = imgAnimationOut
    }

    private fun getStep(): Int {
        var step = 0
        val intent = intent
        val bundle = intent.extras
        if (bundle != null) {
            step = bundle.getInt(INITIAL_STEP_KEY, 0)
        }
        return step
    }

    companion object {

        private const val INITIAL_STEP_KEY = "INITIAL_STEP_KEY"

        fun start(context: Context, initialStep: Int) {
            val intent = Intent(context, RegisterActivity::class.java)
            intent.putExtra(INITIAL_STEP_KEY, initialStep)
            context.startActivity(intent)
        }
    }
}
