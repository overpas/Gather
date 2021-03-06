package by.overpass.gather.ui.meeting.chat.attachments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.fragment.addToBackStack
import by.overpass.gather.commons.android.fragment.transaction
import by.overpass.gather.ui.base.BaseActivityKt
import by.overpass.gather.ui.dialog.PickImageDialogFragment

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
            transaction()
                    .replace(R.id.flPhotosContainer, PhotosFragment.newInstance())
                    .addToBackStack(false)
                    .commit()
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

        fun start(context: Context) {
            context.startActivity(Intent(context, PhotosActivity::class.java))
        }
    }
}
