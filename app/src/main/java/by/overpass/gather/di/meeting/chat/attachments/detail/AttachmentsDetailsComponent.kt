package by.overpass.gather.di.meeting.chat.attachments.detail

import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.meeting.chat.attachments.PhotosFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [AttachmentsDetailModule::class])
interface AttachmentsDetailsComponent {

    fun inject(photosFragment: PhotosFragment)
}