package by.overpass.gather.di.meeting.chat.attachments.detail

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.meeting.chat.attachments.PhotosViewModel
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