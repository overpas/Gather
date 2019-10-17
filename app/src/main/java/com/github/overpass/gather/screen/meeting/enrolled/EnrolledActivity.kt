package com.github.overpass.gather.screen.meeting.enrolled

import android.os.Bundle
import androidx.lifecycle.Observer
import com.github.overpass.gather.App.Companion.appComponentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.di.enrolled.EnrolledComponent
import com.github.overpass.gather.di.enrolled.EnrolledComponentManager
import com.github.overpass.gather.screen.base.BaseActivity
import kotlinx.android.synthetic.main.activity_enrolled.*

class EnrolledActivity : BaseActivity<EnrolledViewModel, EnrolledComponent>() {

    override val componentManager: EnrolledComponentManager
        get() = appComponentManager.getEnrolledComponentManager()

    override fun createComponent(): EnrolledComponent = componentManager.getOrCreate(Unit)

    override fun onComponentCreated(component: EnrolledComponent) {
        component.inject(this)
    }

    override val layoutRes: Int = R.layout.activity_enrolled

    override fun createViewModel(): EnrolledViewModel {
        return viewModelProvider.get(EnrolledViewModel::class.java)
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
}
