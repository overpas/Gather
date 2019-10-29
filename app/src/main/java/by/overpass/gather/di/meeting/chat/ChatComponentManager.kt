package by.overpass.gather.di.meeting.chat

import android.util.Log
import androidx.lifecycle.Lifecycle
import by.overpass.gather.di.meeting.chat.attachments.AttachmentsComponent
import by.overpass.gather.di.meeting.chat.attachments.AttachmentsComponentManager
import by.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import by.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import by.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import by.overpass.gather.di.meeting.chat.users.UsersComponent
import by.overpass.gather.commons.android.lifecycle.LifecycleDisposable
import by.overpass.gather.ui.meeting.chat.ChatFragment

class ChatComponentManager(
        private val chatComponent: ChatComponent
) {

    init {
        Log.w(this::class.java.simpleName, "ChatComponentManager Created")
    }

    private lateinit var attachmentsComponentManagerDisposable: LifecycleDisposable<AttachmentsComponentManager>

    fun getDeleteMessageComponent(): DeleteMessageComponent =
            chatComponent.getDeleteMessageComponent()

    fun getMeetingDetailsComponent(): MeetingDetailComponent =
            chatComponent.getMeetingDetailsComponent()

    fun getUsersComponent(): UsersComponent = chatComponent.getUsersComponent()

    fun getAttachmentsComponent(lifecycle: Lifecycle): AttachmentsComponent {
        attachmentsComponentManagerDisposable = LifecycleDisposable(
                lifecycle,
                AttachmentsComponentManager(chatComponent.getAttachmentsComponent())
        )
        return attachmentsComponentManagerDisposable.value!!
    }

    fun getAttachmentDetailComponent(): AttachmentsDetailsComponent =
            attachmentsComponentManagerDisposable.value!!.getDetailComponent()

    fun inject(chatFragment: ChatFragment) = chatComponent.inject(chatFragment)

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "ChatComponentManager Destroyed")
    }
}