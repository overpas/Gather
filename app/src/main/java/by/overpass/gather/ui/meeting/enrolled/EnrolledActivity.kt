package by.overpass.gather.ui.meeting.enrolled

import android.os.Bundle
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.lifecycle.on
import by.overpass.gather.ui.base.BaseActivityKt
import kotlinx.android.synthetic.main.activity_enrolled.*

class EnrolledActivity : BaseActivityKt<EnrolledViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_enrolled
    }

    override fun createViewModel(): EnrolledViewModel {
        return viewModelProvider.get(EnrolledViewModel::class.java)
    }

    override fun inject() {
        componentManager.getEnrolledComponent()
                .inject(this)
    }

    override fun onBind() {
        on(viewModel.playAnimation()) {
            lavLargeTick.playAnimation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnOk.setOnClickListener {
            finish()
        }
    }
}
