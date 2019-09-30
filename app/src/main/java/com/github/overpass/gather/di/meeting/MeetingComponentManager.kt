package com.github.overpass.gather.di.meeting

import android.util.Log
import com.github.overpass.gather.di.meeting.chat.ChatComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponent
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import com.github.overpass.gather.di.meeting.chat.users.UsersComponent
import com.github.overpass.gather.di.meeting.join.JoinComponent
import com.github.overpass.gather.screen.meeting.MeetingActivity

class MeetingComponentManager(
        private val meetingComponent: MeetingComponent
) : MeetingComponent {

    init {
        Log.w(this::class.java.simpleName, "MeetingComponentManager Created")
    }

    private var chatComponentManager: ChatComponentManager? = null
    private var joinComponent: JoinComponent? = null

    override fun getChatComponent(): ChatComponentManager =
            chatComponentManager ?: meetingComponent.getChatComponent()
                    .also { chatComponentManager = ChatComponentManager(it) }
                    .let { chatComponentManager!! }

    fun clearChatComponent() {
        chatComponentManager = null
    }

    override fun getJoinComponent(): JoinComponent =
            joinComponent ?: meetingComponent.getJoinComponent()
                    .also { joinComponent = it }

    fun clearJoinComponent() {
        joinComponent = null
    }

    fun getUsersComponent(): UsersComponent = getChatComponent().getUsersComponent()

    fun clearUsersComponent() {
        getChatComponent().clearUsersComponent()
    }

    fun getMeetingDetailComponent(): MeetingDetailComponent =
            getChatComponent().getMeetingDetailsComponent()

    fun clearMeetingDetailsComponent() = getChatComponent().clearMeetingDetailsComponent()

    fun getDeleteMessageComponent(): DeleteMessageComponent =
            getChatComponent().getDeleteMessageComponent()

    fun clearDeleteMessageComponent() = getChatComponent().clearDeleteMessageComponent()

    fun getAttachmentsComponent(): AttachmentsComponent =
            getChatComponent().getAttachmentsComponent()

    fun clearAttachmentsComponent() = getChatComponent().clearAttachmentsComponent()

    fun getAttachmentDetailComponent(): AttachmentsDetailsComponent =
            getChatComponent().getAttachmentDetailComponent()

    fun cleatAttachmentDetailComponent() = getChatComponent().clearAttachmentDetailsComponent()

    override fun inject(meetingActivity: MeetingActivity) = meetingComponent.inject(meetingActivity)

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "MeetingComponentManager Destroyed")
    }
}