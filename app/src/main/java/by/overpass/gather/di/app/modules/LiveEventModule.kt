package by.overpass.gather.di.app.modules

import com.hadilq.liveevent.LiveEvent
import dagger.Module
import dagger.Provides

@Module
object LiveEventModule {

    @Provides
    @JvmStatic
    @JvmSuppressWildcards
    fun emptyLiveEvent(): LiveEvent<Unit> = LiveEvent()

    @Provides
    @JvmStatic
    @JvmSuppressWildcards
    fun stringLiveEvent(): LiveEvent<String> = LiveEvent()
}