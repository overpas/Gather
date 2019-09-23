package com.github.overpass.gather.di.meeting.chat.attachments

import android.util.Log
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import com.github.overpass.gather.screen.meeting.chat.attachments.PhotosActivity

class AttachmentsComponentManager(
        private val attachmentsComponent: AttachmentsComponent
): AttachmentsComponent {

    init {
        Log.d(this::class.java.simpleName, "MeetingComponentManager Created")
    }

    override fun getDetailComponent(): AttachmentsDetailsComponent =
            attachmentsComponent.getDetailComponent()

    override fun inject(photosActivity: PhotosActivity) =
            attachmentsComponent.inject(photosActivity)

    protected fun finalize() {
        Log.d(this::class.java.simpleName, "ChatComponentManager Destroyed")
    }
}