package com.github.overpass.gather.di.meeting.chat

import androidx.lifecycle.MutableLiveData
import com.github.overpass.gather.di.ParentScope
import dagger.Module
import dagger.Provides

@Module
object IntegerMutableLiveDataModule {

    @Provides
    @ChatScope
    @JvmStatic
    fun provideIntegerMutableLiveData(): MutableLiveData<Integer> = MutableLiveData()
}