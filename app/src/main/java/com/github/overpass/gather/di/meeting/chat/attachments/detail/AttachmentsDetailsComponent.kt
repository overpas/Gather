package com.github.overpass.gather.di.meeting.chat.attachments.detail

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.ui.meeting.chat.attachments.PhotosFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [AttachmentsDetailModule::class])
interface AttachmentsDetailsComponent {

    fun inject(photosFragment: PhotosFragment)
}