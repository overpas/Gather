package by.overpass.gather.di.meeting.chat

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.meeting.chat.ChatViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatModule {

    @Binds
    @IntoMap
    @ChatScope
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindChatViewModel(viewModel: ChatViewModel): ViewModel
}