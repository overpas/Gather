package by.overpass.gather.di.closeup

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.meeting.chat.attachments.closeup.CloseupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CloseupModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(CloseupViewModel::class)
    abstract fun bindCloseupViewModel(viewModel: CloseupViewModel): ViewModel
}
