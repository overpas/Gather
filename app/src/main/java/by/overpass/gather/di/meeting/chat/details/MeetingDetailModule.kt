package by.overpass.gather.di.meeting.chat.details

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.dialog.details.MeetingDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MeetingDetailModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(MeetingDetailsViewModel::class)
    abstract fun bindMeetingDetailsViewModel(viewModel: MeetingDetailsViewModel): ViewModel
}