package com.github.overpass.gather.di.meeting.chat

import android.util.Log
import androidx.lifecycle.Lifecycle
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponent
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import com.github.overpass.gather.di.meeting.chat.users.UsersComponent
import com.github.overpass.gather.model.commons.LifecycleDisposable
import com.github.overpass.gather.screen.meeting.chat.ChatFragment

class ChatComponentManager(
        private val chatComponent: ChatComponent
) : ChatComponent {

    init {
        Log.d(this::class.java.simpleName, "ChatComponentManager Created")
    }

    private lateinit var attachmentsComponentManagerDisposable: LifecycleDisposable<AttachmentsComponentManager>

    override fun getDeleteMessageComponent(): DeleteMessageComponent =
            chatComponent.getDeleteMessageComponent()

    override fun getMeetingDetailsComponent(): MeetingDetailComponent =
            chatComponent.getMeetingDetailsComponent()

    override fun getUsersComponent(): UsersComponent = chatComponent.getUsersComponent()

    override fun getAttachmentsComponent(): AttachmentsComponent {
        throw IllegalStateException("The subcomponent hasn't been initialized. Consider calling" +
                "fun getAttachmentsComponent(lifecycle: Lifecycle) instead")
    }

    fun getAttachmentsComponent(lifecycle: Lifecycle): AttachmentsComponent {
        attachmentsComponentManagerDisposable = LifecycleDisposable(
                lifecycle,
                AttachmentsComponentManager(chatComponent.getAttachmentsComponent())
        )
        return attachmentsComponentManagerDisposable.value!!
    }

    fun getAttachmentDetailComponent(): AttachmentsDetailsComponent =
            attachmentsComponentManagerDisposable.value!!.getDetailComponent()

    override fun inject(chatFragment: ChatFragment) = chatComponent.inject(chatFragment)

    protected fun finalize() {
        Log.d(this::class.java.simpleName, "ChatComponentManager Destroyed")
    }
}