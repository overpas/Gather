package by.overpass.gather.di.meeting.chat

import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides

@Module
object IntegerMutableLiveDataModule {

    @Provides
    @ChatScope
    @JvmStatic
    fun provideIntegerMutableLiveData(): MutableLiveData<Integer> = MutableLiveData()
}