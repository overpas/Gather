package com.github.overpass.gather.screen.meeting.chat.attachments.closeup

import android.content.Context
import com.bumptech.glide.Glide
import com.github.overpass.gather.App.Companion.appComponentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.getStringExtra
import com.github.overpass.gather.screen.base.BaseActivityKt
import kotlinx.android.synthetic.main.activity_closeup.*

class CloseupActivity : BaseActivityKt<CloseupViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_closeup
    }

    override fun createViewModel(): CloseupViewModel {
        return viewModelProvider.get(CloseupViewModel::class.java)
    }

    override fun inject() {
        appComponentManager.getCloseupComponent()
                .inject(this)
    }

    override fun onBind() {
        super.onBind()
        Glide.with(this)
                .load(getStringExtra(PHOTO_URL_KEY))
                .into(pvPhoto)
    }

    override fun clearComponent() {
        super.clearComponent()
        appComponentManager.clearCloseupComponent()
    }

    companion object {

        private const val PHOTO_URL_KEY = "PHOTO_URL_KEY"

        @JvmStatic
        fun start(context: Context, photoUrl: String) {
            start(context, CloseupActivity::class.java, PHOTO_URL_KEY, photoUrl)
        }
    }
}
