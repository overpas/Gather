package com.github.overpass.gather.di.meeting

import android.util.Log
import androidx.lifecycle.Lifecycle
import com.github.overpass.gather.di.meeting.chat.ChatComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponent
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import com.github.overpass.gather.di.meeting.chat.users.UsersComponent
import com.github.overpass.gather.di.meeting.join.JoinComponent
import com.github.overpass.gather.model.commons.LifecycleDisposable

class MeetingComponentManager(
        private val meetingComponentFactory: MeetingComponent.Factory
) : MeetingComponent.Factory {

    init {
        Log.w(this::class.java.simpleName, "MeetingComponentManager Created")
    }

    private lateinit var meetingComponent: MeetingComponent
    private lateinit var chatComponentManagerDisposable: LifecycleDisposable<ChatComponentManager>

    override fun create(meetingId: String): MeetingComponent {
        meetingComponent = meetingComponentFactory.create(meetingId)
        return meetingComponent
    }

    fun getChatComponent(lifecycle: Lifecycle): ChatComponentManager {
        chatComponentManagerDisposable = LifecycleDisposable(
                lifecycle,
                ChatComponentManager(meetingComponent.getChatComponent())
        )
        return chatComponentManagerDisposable.value!!
    }

    fun getJoinComponent(): JoinComponent = meetingComponent.getJoinComponent()

    fun getUsersComponent(): UsersComponent =
            chatComponentManagerDisposable.value!!.getUsersComponent()

    fun getMeetingDetailComponent(): MeetingDetailComponent =
            chatComponentManagerDisposable.value!!.getMeetingDetailsComponent()

    fun getDeleteMessageComponent(): DeleteMessageComponent =
            chatComponentManagerDisposable.value!!.getDeleteMessageComponent()

    fun getAttachmentsComponent(lifecycle: Lifecycle): AttachmentsComponent =
            chatComponentManagerDisposable.value!!.getAttachmentsComponent(lifecycle)

    fun getAttachmentDetailComponent(): AttachmentsDetailsComponent =
            chatComponentManagerDisposable.value!!.getAttachmentDetailComponent()

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "MeetingComponentManager Destroyed")
    }
}