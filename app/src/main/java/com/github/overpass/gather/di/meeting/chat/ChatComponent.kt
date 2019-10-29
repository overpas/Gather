package com.github.overpass.gather.di.meeting.chat

import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponent
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import com.github.overpass.gather.di.meeting.chat.users.UsersComponent
import com.github.overpass.gather.ui.meeting.chat.ChatFragment
import dagger.Subcomponent

@ChatScope
@Subcomponent(modules = [
    ChatModule::class,
    IntegerMutableLiveDataModule::class
])
interface ChatComponent {

    fun getDeleteMessageComponent(): DeleteMessageComponent

    fun getMeetingDetailsComponent(): MeetingDetailComponent

    fun getUsersComponent(): UsersComponent

    fun getAttachmentsComponent(): AttachmentsComponent

    fun inject(chatFragment: ChatFragment)
}