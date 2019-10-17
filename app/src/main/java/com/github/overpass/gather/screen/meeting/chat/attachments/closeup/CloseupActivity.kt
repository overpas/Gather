package com.github.overpass.gather.screen.meeting.chat.attachments.closeup

import android.content.Context
import com.bumptech.glide.Glide
import com.github.overpass.gather.App.Companion.appComponentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.di.closeup.CloseupComponent
import com.github.overpass.gather.di.closeup.CloseupComponentManager
import com.github.overpass.gather.model.commons.getStringExtra
import com.github.overpass.gather.screen.base.BaseActivity
import kotlinx.android.synthetic.main.activity_closeup.*

class CloseupActivity : BaseActivity<CloseupViewModel, CloseupComponent>() {

    override val layoutRes: Int = R.layout.activity_closeup

    override val componentManager: CloseupComponentManager
        get() = appComponentManager.getCloseupComponentManager()

    override fun createComponent(): CloseupComponent = componentManager.getOrCreate(Unit)

    override fun onComponentCreated(component: CloseupComponent) {
        component.inject(this)
    }

    override fun createViewModel(): CloseupViewModel {
        return viewModelProvider.get(CloseupViewModel::class.java)
    }

    override fun onBind() {
        super.onBind()
        Glide.with(this)
                .load(getStringExtra(PHOTO_URL_KEY))
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
