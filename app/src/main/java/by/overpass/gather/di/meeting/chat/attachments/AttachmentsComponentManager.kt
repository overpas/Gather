package by.overpass.gather.di.meeting.chat.attachments

import android.util.Log
import by.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import by.overpass.gather.ui.meeting.chat.attachments.PhotosActivity

class AttachmentsComponentManager(
        private val attachmentsComponent: AttachmentsComponent
): AttachmentsComponent {

    init {
        Log.w(this::class.java.simpleName, "MeetingComponentManager Created")
    }

    override fun getDetailComponent(): AttachmentsDetailsComponent =
            attachmentsComponent.getDetailComponent()

    override fun inject(photosActivity: PhotosActivity) =
            attachmentsComponent.inject(photosActivity)

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "ChatComponentManager Destroyed")
    }
}