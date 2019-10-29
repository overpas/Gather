package com.github.overpass.gather.ui.meeting.chat.attachments.closeup

import android.content.Context
import com.bumptech.glide.Glide
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.ui.base.BaseActivityKt
import kotlinx.android.synthetic.main.activity_closeup.*

class CloseupActivity : BaseActivityKt<CloseupViewModel>() {

    private fun getPhotoUrl(): String {
        return intent.extras
                ?.getString(PHOTO_URL_KEY)
                ?: "nothingness"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_closeup
    }

    override fun createViewModel(): CloseupViewModel {
        return viewModelProvider.get(CloseupViewModel::class.java)
    }

    override fun inject() {
        componentManager.getCloseupComponent()
                .inject(this)
    }

    override fun onBind() {
        super.onBind()
        Glide.with(this)
                .load(getPhotoUrl())
                .into(pvPhoto)
    }

    companion object {

        private const val PHOTO_URL_KEY = "PHOTO_URL_KEY"

        @JvmStatic
        fun start(context: Context, photoUrl: String) {
            start(context, CloseupActivity::class.java, PHOTO_URL_KEY, photoUrl)
        }
    }
}
