package com.github.overpass.gather.di.meeting.chat.attachments

import com.github.overpass.gather.di.ParentScope
import com.github.overpass.gather.di.image.ImageSourceModule
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import com.github.overpass.gather.screen.meeting.chat.attachments.PhotosActivity
import dagger.Subcomponent

@ParentScope
@Subcomponent(modules = [AttachmentsModule::class, ImageSourceModule::class])
interface AttachmentsComponent {

    fun getDetailComponent(): AttachmentsDetailsComponent

    fun inject(photosActivity: PhotosActivity)
}