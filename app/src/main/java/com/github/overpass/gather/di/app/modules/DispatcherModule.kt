package com.github.overpass.gather.di.app.modules

import com.github.overpass.gather.di.DEFAULT_DISPATCHER
import com.github.overpass.gather.di.IO_DISPATCHER
import com.github.overpass.gather.di.MAIN_DISPATCHER
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
object DispatcherModule {

    @Provides
    @Singleton
    @JvmStatic
    @Named(MAIN_DISPATCHER)
    fun provideMain(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    @JvmStatic
    @Named(IO_DISPATCHER)
    fun provideIO(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @JvmStatic
    @Named(DEFAULT_DISPATCHER)
    fun provideDefault(): CoroutineDispatcher = Dispatchers.Default
}