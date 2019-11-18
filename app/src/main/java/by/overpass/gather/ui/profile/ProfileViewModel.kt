package by.overpass.gather.ui.profile

import android.app.Application
import android.net.Uri
import androidx.core.util.Consumer
import androidx.lifecycle.MutableLiveData
import by.overpass.gather.commons.image.ChooseImageHelper
import by.overpass.gather.model.usecase.image.ImageSourceUseCase
import by.overpass.gather.model.usecase.userdata.PersonalDataUseCase
import by.overpass.gather.model.usecase.userdata.ProfileUseCase
import by.overpass.gather.ui.base.personal.DataViewModel
import com.google.firebase.auth.FirebaseUser
import com.hadilq.liveevent.LiveEvent
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        application: Application,
        chooseImageHelper: ChooseImageHelper,
        personalDataUseCase: PersonalDataUseCase,
        chosenImageData: MutableLiveData<Uri>,
        writeDeniedData: LiveEvent<Boolean>,
        readDeniedData: LiveEvent<Boolean>,
        imageSourceUseCase: ImageSourceUseCase,
        private val profileUseCase: ProfileUseCase
) : DataViewModel(
        application,
        chooseImageHelper,
        personalDataUseCase,
        chosenImageData,
        writeDeniedData,
        readDeniedData,
        imageSourceUseCase
) {

    private var isEditMode = false

    fun getUserData(onSuccess: Consumer<FirebaseUser>, onError: Runnable) {
        profileUseCase.getUserData(onSuccess, onError)
    }

    fun signOut(onSuccess: Runnable) {
        profileUseCase.signOut(onSuccess)
    }

    fun onProfileModeChanged(onChange: Consumer<Boolean>) {
        isEditMode = !isEditMode
        onChange.accept(isEditMode)
    }

    fun checkIfIsEditMode(): Boolean {
        val value = isEditMode
        if (isEditMode) {
            isEditMode = false
        }
        return value
    }
}
