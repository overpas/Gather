package com.github.overpass.gather.di.meeting.chat.attachments

import android.util.Log
import com.github.overpass.gather.di.ComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentDetailsComponentManager

class AttachmentsComponentManager(
        attachmentsComponent: AttachmentsComponent
) : ComponentManager<Unit, AttachmentsComponent>({ attachmentsComponent }) {

    init {
        Log.w(this::class.java.simpleName, "AttachmentsComponentManager Created")
    }

    private var detailsComponentManager: AttachmentDetailsComponentManager? = null

    fun getDetailComponentManager(): AttachmentDetailsComponentManager = detailsComponentManager
            ?: AttachmentDetailsComponentManager(getOrCreate(Unit).getDetailComponent())
                    .also { detailsComponentManager = it }

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "AttachmentsComponentManager Destroyed")
    }
}