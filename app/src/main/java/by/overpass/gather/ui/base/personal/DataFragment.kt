package by.overpass.gather.ui.base.personal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import by.overpass.gather.R
import by.overpass.gather.commons.android.lifecycle.on
import by.overpass.gather.commons.android.lifecycle.onMaybeNull
import by.overpass.gather.commons.android.snackbar
import by.overpass.gather.commons.android.text
import by.overpass.gather.ui.auth.register.RegistrationFragment
import by.overpass.gather.ui.auth.register.add.AddDataStatus
import by.overpass.gather.ui.auth.register.add.ImageSource
import by.overpass.gather.ui.dialog.PickImageDialogFragment
import by.overpass.gather.ui.dialog.progress.indeterminate.ProgressDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_personal_data.*

// TODO: The hierarchy is messed up
abstract class DataFragment<VM : DataViewModel> : RegistrationFragment<VM>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ivPhotoPreview.setOnLongClickListener {
            viewModel.resetChosenImage()
            true
        }
    }

    override fun onBind() {
        super.onBind()
        onMaybeNull(viewModel.getChosenImageData()) {
            handleChosenImageUri(it)
        }
        on(viewModel.imageSourceData) {
            handleImageSource(it)
        }
        on(viewModel.getWritePermissionDeniedData()) {
            onPermissionChanged(it)
        }
        on(viewModel.getReadPermissionDeniedData()) {
            onPermissionChanged(it)
        }
    }

    protected fun onPermissionChanged(denied: Boolean) {
        if (denied) {
            ivPhotoPreview!!.snackbar("Sorry, you can't choose photo without permissions", Snackbar.LENGTH_SHORT)
        }
    }

    protected fun handleImageSource(imageSource: ImageSource) {
        if (imageSource == ImageSource.GALLERY) {
            viewModel.chooseFromGallery(requireActivity(), this)
        } else if (imageSource == ImageSource.CAMERA) {
            viewModel.chooseFromCamera(requireActivity(), this)
        }
    }

    protected open fun handleChosenImageUri(uri: Uri?) {
        if (uri != null) {
            ivPhotoPreview.scaleX = 1f
            ivPhotoPreview.scaleY = 1f
            ivPhotoPreview.scaleType = ImageView.ScaleType.CENTER_CROP
            ivPhotoPreview.setImageURI(uri)
            lavTick.visibility = View.VISIBLE
            lavTick.playAnimation()
        } else { // reset
            ivPhotoPreview.scaleY = 2f
            ivPhotoPreview.scaleX = 2f
            ivPhotoPreview.scaleType = ImageView.ScaleType.CENTER
            ivPhotoPreview.setImageResource(R.drawable.ic_add_pic)
            lavTick.visibility = View.GONE
        }
    }

    protected open fun onChooseImageClick() {
        PickImageDialogFragment.show(fragmentManager)
    }

    fun onSubmitClick() {
        viewModel.submit(context!!.contentResolver, tietUsername.text())
                .observe(viewLifecycleOwner, Observer<AddDataStatus> { this.handleAddDataStatus(it) })
    }

    private fun handleAddDataStatus(addDataStatus: AddDataStatus) {
        when (addDataStatus.tag()) {
            AddDataStatus.PROGRESS -> handleProgress(addDataStatus.`as`<AddDataStatus.Progress>(AddDataStatus.Progress::class.java))
            AddDataStatus.INVALID_USERNAME -> handleInvalidUsername(addDataStatus.`as`<AddDataStatus.InvalidUsername>(AddDataStatus.InvalidUsername::class.java))
            AddDataStatus.ERROR -> handleError(addDataStatus.`as`<AddDataStatus.Error>(AddDataStatus.Error::class.java))
            AddDataStatus.SUCCESS -> handleSuccess(addDataStatus.`as`<AddDataStatus.Success>(AddDataStatus.Success::class.java))
        }
    }

    protected fun handleProgress(progress: AddDataStatus.Progress) {
        ProgressDialogFragment.show(fragmentManager)
    }

    protected fun handleInvalidUsername(invalidUsername: AddDataStatus.InvalidUsername) {
        ProgressDialogFragment.hide(fragmentManager)
        tietUsername.error = "Invalid username"
    }

    protected fun handleError(error: AddDataStatus.Error) {
        ProgressDialogFragment.hide(fragmentManager)
        val message = error.throwable.localizedMessage
        ivPhotoPreview.snackbar(message, Snackbar.LENGTH_SHORT)
    }

    protected open fun handleSuccess(success: AddDataStatus.Success) {
        ProgressDialogFragment.hide(fragmentManager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onImageChosen(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
