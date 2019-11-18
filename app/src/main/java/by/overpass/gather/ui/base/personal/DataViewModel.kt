package by.overpass.gather.ui.base.personal

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.overpass.gather.commons.image.ChooseImageHelper
import by.overpass.gather.model.usecase.image.ImageSourceUseCase
import by.overpass.gather.model.usecase.userdata.PersonalDataUseCase
import by.overpass.gather.ui.auth.register.add.AddDataStatus
import by.overpass.gather.ui.auth.register.add.ImageSource
import com.hadilq.liveevent.LiveEvent

// TODO: It shouldn't be a subclass of RegistrationStepViewModel, but there's no time to refactor
//  the whole hierarchy
abstract class DataViewModel(
        application: Application,
        private val chooseImageHelper: ChooseImageHelper,
        private val personalDataUseCase: PersonalDataUseCase,
        protected val chosenImageData: MutableLiveData<Uri>,
        private val writePermissionDeniedData: LiveEvent<Boolean>,
        private val readPermissionDeniedData: LiveEvent<Boolean>,
        private var imageSourceUseCase: ImageSourceUseCase? = null
) : AndroidViewModel(application) {

    private var contentUriFromCamera: Uri? = null

    val imageSourceData: LiveData<ImageSource>
        get() = imageSourceUseCase!!.getImageSourceData()

    protected val selectedUri: Uri?
        get() {
            return if (contentUriFromCamera != null) {
                contentUriFromCamera
            } else {
                chosenImageData.value
            }
        }

    fun getChosenImageData(): LiveData<Uri> {
        return chosenImageData
    }

    fun getWritePermissionDeniedData(): LiveData<Boolean> {
        return writePermissionDeniedData
    }

    fun getReadPermissionDeniedData(): LiveData<Boolean> {
        return readPermissionDeniedData;
    }

    fun firstAskPermissionsThenRun(activity: FragmentActivity,
                                   fragment: Fragment,
                                   requestCode: Int,
                                   action: () -> Unit) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            requestPermissions(fragment, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestCode)
        } else {
            // Permission has already been granted
            action()
        }
    }

    fun chooseFromGallery(activity: FragmentActivity, fragment: Fragment) {
        firstAskPermissionsThenRun(activity, fragment, REQUEST_CODE_FROM_GALLERY, { chooseFromGallery(fragment) })
    }

    private fun chooseFromGallery(fragment: Fragment) {
        chooseImageHelper.chooseFromGallery(fragment, REQUEST_CODE_FROM_GALLERY)
    }

    fun onImageChosen(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_FROM_GALLERY ->
                    //data.getData returns the content URI for the selected Image
                    imageFromGalleryChosen(data)
                REQUEST_CODE_FROM_CAMERA -> imageFromCameraChosen()
            }

        }
    }

    private fun imageFromCameraChosen() {
        chosenImageData.value = chosenImageData.value
    }

    private fun imageFromGalleryChosen(data: Intent?) {
        if (data != null) {
            chosenImageData.value = data.data
        }
    }

    private fun chooseFromCamera(fragment: Fragment) {
        val file = chooseImageHelper.chooseFromCamera(fragment, REQUEST_CODE_FROM_CAMERA)
        val imageUri = Uri.parse("file://" + file!!.absolutePath)
        chosenImageData.value = imageUri
        MediaScannerConnection.scanFile(fragment.context, arrayOf(file.absolutePath), null) { path, uri -> contentUriFromCamera = uri }
    }

    fun resetChosenImage() {
        contentUriFromCamera = null
        chosenImageData.value = null
    }

    fun chooseFromCamera(activity: FragmentActivity, fragment: Fragment) {
        firstAskPermissionsThenRun(activity, fragment, REQUEST_CODE_FROM_CAMERA, { chooseFromCamera(fragment) })
    }

    private fun requestPermissions(fragment: Fragment, permissions: Array<String>, requestCode: Int) {
        fragment.requestPermissions(permissions, requestCode)
    }

    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray,
                                   fragment: Fragment) {
        when (requestCode) {
            REQUEST_CODE_FROM_CAMERA -> {
                handlePermissionResult(grantResults, writePermissionDeniedData, fragment) {
                    chooseFromCamera(fragment)
                }
            }
            REQUEST_CODE_FROM_GALLERY -> {
                handlePermissionResult(grantResults, readPermissionDeniedData, fragment) {
                    chooseFromGallery(fragment)
                }
            }
        }// other 'case' lines to check for other
        // permissions this app might request.
    }

    private fun handlePermissionResult(grantResults: IntArray,
                                       permissionDeniedData: LiveEvent<Boolean>,
                                       fragment: Fragment,
                                       action: () -> Unit) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            action()
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            permissionDeniedData.setValue(true)
        }
    }

    fun submit(contentResolver: ContentResolver, username: String): LiveData<AddDataStatus> {
        return personalDataUseCase.submit(contentResolver, username, selectedUri)
    }

    companion object {

        private const val REQUEST_CODE_FROM_GALLERY = 12
        private const val REQUEST_CODE_FROM_CAMERA = 13
    }
}
