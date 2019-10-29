package by.overpass.gather.di.meeting.chat.attachments

import by.overpass.gather.di.ParentScope
import by.overpass.gather.di.image.ImageSourceModule
import by.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import by.overpass.gather.ui.meeting.chat.attachments.PhotosActivity
import dagger.Subcomponent

@ParentScope
@Subcomponent(modules = [AttachmentsModule::class, ImageSourceModule::class])
interface AttachmentsComponent {

    fun getDetailComponent(): AttachmentsDetailsComponent

    fun inject(photosActivity: PhotosActivity)
}