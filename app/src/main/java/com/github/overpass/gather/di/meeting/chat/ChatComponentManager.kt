package com.github.overpass.gather.di.meeting.chat

import android.util.Log
import com.github.overpass.gather.di.ComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentDetailsComponentManager
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponentManager
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponentManager
import com.github.overpass.gather.di.meeting.chat.users.UsersComponentManager

class ChatComponentManager(
        chatComponent: ChatComponent
) : ComponentManager<Unit, ChatComponent>({ chatComponent }) {

    init {
        Log.w(this::class.java.simpleName, "ChatComponentManager Created")
    }

    private var attachmentsComponentManager: AttachmentsComponentManager? = null
    private var deleteMessageComponentManager: DeleteMessageComponentManager? = null
    private var meetingDetailComponentManager: MeetingDetailComponentManager? = null
    private var usersComponentManager: UsersComponentManager? = null

    fun getDeleteMessageComponentManager(): DeleteMessageComponentManager = deleteMessageComponentManager
            ?: DeleteMessageComponentManager(getOrCreate(Unit).getDeleteMessageComponent())
                    .also { deleteMessageComponentManager = it }

    fun getMeetingDetailsComponentManager(): MeetingDetailComponentManager = meetingDetailComponentManager
            ?: MeetingDetailComponentManager(getOrCreate(Unit).getMeetingDetailsComponent())
                    .also { meetingDetailComponentManager = it }

    fun getUsersComponentManager(): UsersComponentManager = usersComponentManager
            ?: UsersComponentManager(getOrCreate(Unit).getUsersComponent())
                    .also { usersComponentManager = it }

    fun getAttachmentsComponentManager(): AttachmentsComponentManager = attachmentsComponentManager
            ?: AttachmentsComponentManager(getOrCreate(Unit).getAttachmentsComponent())
                    .also { attachmentsComponentManager = it }

    fun getAttachmentDetailComponentManager(): AttachmentDetailsComponentManager =
            getAttachmentsComponentManager().getDetailComponentManager()

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "ChatComponentManager Destroyed")
    }
}