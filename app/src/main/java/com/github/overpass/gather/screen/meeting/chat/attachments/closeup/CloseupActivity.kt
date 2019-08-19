package com.github.overpass.gather.screen.meeting.chat.attachments.closeup

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.github.overpass.gather.R
import com.github.overpass.gather.screen.base.BaseActivity
import kotlinx.android.synthetic.main.activity_closeup.*

class CloseupActivity : BaseActivity<CloseUpViewModel>() {

    private fun getPhotoUrl(): String = intent.extras
            ?.run { getString(PHOTO_URL_KEY) }
            ?: "nothingness"

    override fun getLayoutRes(): Int {
        return R.layout.activity_closeup
    }

    override fun createViewModel(): CloseUpViewModel {
        return ViewModelProviders.of(this).get(CloseUpViewModel::class.java)
    }

    override fun onBind() {
        super.onBind()
        pvPhoto.load(getPhotoUrl())
    }

    companion object {

        private const val PHOTO_URL_KEY = "PHOTO_URL_KEY"

        fun start(context: Context, photoUrl: String) {
            start(context, CloseupActivity::class.java, PHOTO_URL_KEY, photoUrl)
        }
    }
}
