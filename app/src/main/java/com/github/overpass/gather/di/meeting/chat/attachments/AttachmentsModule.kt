package com.github.overpass.gather.di.meeting.chat.attachments

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ParentScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.ui.meeting.chat.attachments.GeneralPhotoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AttachmentsModule {

    @Binds
    @IntoMap
    @ParentScope
    @ViewModelKey(GeneralPhotoViewModel::class)
    abstract fun bindGeneralPhotoViewModel(viewModel: GeneralPhotoViewModel): ViewModel
}