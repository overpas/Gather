package com.github.overpass.gather.screen.meeting.chat.attachments

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.Constants.MEETING_ID_KEY
import com.github.overpass.gather.model.commons.FragmentUtils
import com.github.overpass.gather.model.commons.getStringExtra
import com.github.overpass.gather.screen.base.BaseActivityKt
import com.github.overpass.gather.screen.dialog.PickImageDialogFragment

class PhotosActivity : BaseActivityKt<GeneralPhotoViewModel>(), PickImageDialogFragment.OnClickListener {

    override fun getLayoutRes(): Int {
        return R.layout.activity_photos
    }

    override fun createViewModel(): GeneralPhotoViewModel {
        return viewModelProvider.get(GeneralPhotoViewModel::class.java)
    }

    override fun inject() {
        componentManager.getAttachmentsComponent(lifecycle)
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            FragmentUtils.replace(supportFragmentManager, R.id.flPhotosContainer,
                    PhotosFragment.newInstance(getStringExtra(MEETING_ID_KEY)), false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActionBar(actionBar: ActionBar) {
        val primaryColor = ContextCompat.getColor(this, R.color.colorPrimary)
        actionBar.setBackgroundDrawable(ColorDrawable(primaryColor))
        actionBar.setTitle(R.string.attachments)
    }

    override fun onGallery() {
        viewModel.onGallery()
    }

    override fun onCamera() {
        viewModel.onCamera()
    }

    companion object {

        fun start(context: Context, meetingId: String) {
            start(context, PhotosActivity::class.java, MEETING_ID_KEY, meetingId)
        }
    }
}
