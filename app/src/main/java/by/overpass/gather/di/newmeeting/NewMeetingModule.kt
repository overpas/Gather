package by.overpass.gather.di.newmeeting

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.create.NewMeetingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NewMeetingModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(NewMeetingViewModel::class)
    abstract fun bindNewMeetingViewModel(viewModel: NewMeetingViewModel): ViewModel
}