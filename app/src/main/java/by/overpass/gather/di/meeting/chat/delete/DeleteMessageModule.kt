package by.overpass.gather.di.meeting.chat.delete

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.dialog.delete.DeleteMessageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DeleteMessageModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(DeleteMessageViewModel::class)
    abstract fun bindDeleteMessageViewModel(viewModel: DeleteMessageViewModel): ViewModel
}