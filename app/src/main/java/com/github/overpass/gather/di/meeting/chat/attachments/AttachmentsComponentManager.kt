package com.github.overpass.gather.di.meeting.chat.attachments

import android.util.Log
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import com.github.overpass.gather.screen.meeting.chat.attachments.PhotosActivity

class AttachmentsComponentManager(
        private val attachmentsComponent: AttachmentsComponent
) : AttachmentsComponent {

    init {
        Log.w(this::class.java.simpleName, "AttachmentsComponentManager Created")
    }

    private var detailsComponent: AttachmentsDetailsComponent? = null

    override fun getDetailComponent(): AttachmentsDetailsComponent =
            detailsComponent ?: attachmentsComponent.getDetailComponent()
                    .also { detailsComponent = it }

    fun clearDetailComponent() {
        detailsComponent = null
    }

    override fun inject(photosActivity: PhotosActivity) =
            attachmentsComponent.inject(photosActivity)

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "AttachmentsComponentManager Destroyed")
    }
}