package by.overpass.gather.di.map.detail

import android.location.Location
import androidx.lifecycle.MutableLiveData
import by.overpass.gather.di.ViewScope
import by.overpass.gather.ui.map.detail.BaseMapDetailViewModel
import com.hadilq.liveevent.LiveEvent
import dagger.Module
import dagger.Provides

@Module
object MapDetailsModule {

    @Provides
    @ViewScope
    @JvmStatic
    fun locationLiveData(): MutableLiveData<Location> = MutableLiveData()

    @Provides
    @ViewScope
    @JvmStatic
    fun fabActionLiveData(): MutableLiveData<BaseMapDetailViewModel.FabAction> = MutableLiveData()

    @Provides
    @ViewScope
    @JvmStatic
    fun permissionsGrantedData(): LiveEvent<Boolean> = LiveEvent()
}