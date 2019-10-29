package by.overpass.gather.di.meeting.chat.users

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.meeting.chat.users.UsersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UsersModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(UsersViewModel::class)
    abstract fun bindUsersViewModel(viewModel: UsersViewModel): ViewModel
}