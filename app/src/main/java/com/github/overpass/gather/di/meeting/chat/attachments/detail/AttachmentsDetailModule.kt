package com.github.overpass.gather.di.meeting.chat.attachments.detail

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.meeting.chat.attachments.PhotosViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AttachmentsDetailModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(PhotosViewModel::class)
    abstract fun bindPhotosViewModel(viewModel: PhotosViewModel): ViewModel
}