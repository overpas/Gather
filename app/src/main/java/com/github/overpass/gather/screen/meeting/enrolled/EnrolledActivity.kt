package com.github.overpass.gather.screen.meeting.enrolled

import android.os.Bundle
import androidx.lifecycle.Observer
import com.github.overpass.gather.App.Companion.appComponentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.screen.base.BaseActivityKt
import kotlinx.android.synthetic.main.activity_enrolled.*

class EnrolledActivity : BaseActivityKt<EnrolledViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_enrolled
    }

    override fun createViewModel(): EnrolledViewModel {
        return viewModelProvider.get(EnrolledViewModel::class.java)
    }

    override fun inject() {
        appComponentManager.getEnrolledComponent()
                .inject(this)
    }

    override fun onBind() {
        viewModel.playAnimation().observe(this, Observer { nothing -> lavLargeTick.playAnimation() })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnOk.setOnClickListener {
            finish()
        }
    }

    override fun clearComponent() {
        super.clearComponent()
        appComponentManager.clearEnrolledComponent()
    }
}
