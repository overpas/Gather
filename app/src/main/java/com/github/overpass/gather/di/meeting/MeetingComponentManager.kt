package com.github.overpass.gather.di.meeting

import android.util.Log
import com.github.overpass.gather.di.ComponentManager
import com.github.overpass.gather.di.meeting.chat.ChatComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentDetailsComponentManager
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponentManager
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponentManager
import com.github.overpass.gather.di.meeting.chat.users.UsersComponentManager
import com.github.overpass.gather.di.meeting.join.JoinComponentManager

class MeetingComponentManager(
        meetingComponent: MeetingComponent
) : ComponentManager<Unit, MeetingComponent>({ meetingComponent }) {

    init {
        Log.w(this::class.java.simpleName, "MeetingComponentManager Created")
    }

    private var chatComponentManager: ChatComponentManager? = null
    private var joinComponentManager: JoinComponentManager? = null

    fun getChatComponentManager(): ChatComponentManager = chatComponentManager
            ?: ChatComponentManager(getOrCreate(Unit).getChatComponent())
                    .also { chatComponentManager = it }

    fun getJoinComponentManager(): JoinComponentManager = joinComponentManager
            ?: JoinComponentManager(getOrCreate(Unit).getJoinComponent())
                    .also { joinComponentManager = it }

    fun getUsersComponentManager(): UsersComponentManager =
            getChatComponentManager().getUsersComponentManager()

    fun getMeetingDetailComponentManager(): MeetingDetailComponentManager =
            getChatComponentManager().getMeetingDetailsComponentManager()

    fun getDeleteMessageComponentManager(): DeleteMessageComponentManager =
            getChatComponentManager().getDeleteMessageComponentManager()

    fun getAttachmentsComponentManager(): AttachmentsComponentManager =
            getChatComponentManager().getAttachmentsComponentManager()

    fun getAttachmentDetailComponentManager(): AttachmentDetailsComponentManager =
            getChatComponentManager().getAttachmentDetailComponentManager()

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "MeetingComponentManager Destroyed")
    }
}