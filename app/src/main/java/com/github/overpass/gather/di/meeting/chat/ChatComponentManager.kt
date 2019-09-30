package com.github.overpass.gather.di.meeting.chat

import android.util.Log
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import com.github.overpass.gather.di.meeting.chat.users.UsersComponent
import com.github.overpass.gather.screen.meeting.chat.ChatFragment

class ChatComponentManager(
        private val chatComponent: ChatComponent
) : ChatComponent {

    init {
        Log.w(this::class.java.simpleName, "ChatComponentManager Created")
    }

    private var attachmentsComponentManager: AttachmentsComponentManager? = null
    private var deleteMessageComponent: DeleteMessageComponent? = null
    private var meetingDetailsComponent: MeetingDetailComponent? = null
    private var usersComponent: UsersComponent? = null

    override fun getDeleteMessageComponent(): DeleteMessageComponent =
            deleteMessageComponent ?: chatComponent.getDeleteMessageComponent()
                    .also { deleteMessageComponent = it }

    fun clearDeleteMessageComponent() {
        deleteMessageComponent = null
    }

    override fun getMeetingDetailsComponent(): MeetingDetailComponent =
            meetingDetailsComponent ?: chatComponent.getMeetingDetailsComponent()
                    .also { meetingDetailsComponent = it }

    fun clearMeetingDetailsComponent() {
        meetingDetailsComponent = null
    }

    override fun getUsersComponent(): UsersComponent =
            usersComponent ?: chatComponent.getUsersComponent()
                    .also { usersComponent = it }

    fun clearUsersComponent() {
        usersComponent = null
    }

    override fun getAttachmentsComponent(): AttachmentsComponentManager =
            attachmentsComponentManager ?: chatComponent.getAttachmentsComponent()
                    .also { attachmentsComponentManager = AttachmentsComponentManager(it) }
                    .let { attachmentsComponentManager!! }

    fun clearAttachmentsComponent() {
        attachmentsComponentManager = null
    }

    fun getAttachmentDetailComponent(): AttachmentsDetailsComponent =
            getAttachmentsComponent().getDetailComponent()

    fun clearAttachmentDetailsComponent() = getAttachmentsComponent().clearDetailComponent()

    override fun inject(chatFragment: ChatFragment) = chatComponent.inject(chatFragment)

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "ChatComponentManager Destroyed")
    }
}