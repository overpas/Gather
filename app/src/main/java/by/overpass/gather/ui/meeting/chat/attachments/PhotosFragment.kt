package by.overpass.gather.ui.meeting.chat.attachments

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.lifecycle.on
import by.overpass.gather.commons.android.snackbar
import by.overpass.gather.ui.base.personal.DataFragment
import by.overpass.gather.ui.dialog.progress.determinate.ProgressPercentDialogFragment
import by.overpass.gather.ui.map.Meeting
import by.overpass.gather.ui.meeting.chat.attachments.closeup.CloseupActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_photos.*

class PhotosFragment : DataFragment<PhotosViewModel>() {

    private lateinit var adapter: PhotosAdapter

    override fun createViewModel(): PhotosViewModel {
        return viewModelProvider.get(PhotosViewModel::class.java)
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_photos
    }

    override fun inject() {
        componentManager.getAttachmentsDetailsComponent()
                .inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupList()
        on(viewModel.getMeeting()) {
            handleMeeting(it)
        }
        on(viewModel.getSuggestToChooseData()) {
            handleChoose(it)
        }
        on(viewModel.photoUploadSuccess()) {
            handleUploadSuccess()
        }
        on(viewModel.photoUploadProgress()) {
            handleUploadProgress(it)
        }
        on(viewModel.photoUploadError()) {
            handleUploadError(it)
        }
        fabAttach.setOnClickListener {
            viewModel.doAction()
        }
    }

    private fun handleUploadProgress(percent: Int) {
        ProgressPercentDialogFragment.progress(requireFragmentManager(), percent)
    }

    private fun handleUploadError(message: String) {
        ProgressPercentDialogFragment.hide(requireFragmentManager())
        ivPhotoPreview.snackbar(message, Snackbar.LENGTH_SHORT)
    }

    private fun handleUploadSuccess() {
        ProgressPercentDialogFragment.hide(requireFragmentManager())
        ivPhotoPreview.snackbar(getString(R.string.success), Snackbar.LENGTH_SHORT)
        viewModel.resetChosenImage()
    }

    private fun handleChoose(shouldSuggestToChoose: Boolean?) {
        if (shouldSuggestToChoose != null && shouldSuggestToChoose) {
            super.onChooseImageClick()
        }
    }

    override fun handleChosenImageUri(uri: Uri?) {
        if (uri != null) {
            ivPhotoPreview.visibility = View.VISIBLE
            ivPhotoPreview.setImageURI(uri)
            fabAttach.setImageResource(R.drawable.ic_send)
        } else {
            ivPhotoPreview.visibility = View.GONE
            fabAttach.setImageResource(R.drawable.ic_attach)
        }
    }

    private fun handleMeeting(meeting: Meeting) {
        val photos = meeting.photos
        adapter.setItems(photos)
    }

    private fun setupList() {
        val layoutManager = StaggeredGridLayoutManager(2,
                RecyclerView.VERTICAL)
        rvAttachments.layoutManager = layoutManager
        adapter = PhotosAdapter { photoUrl -> CloseupActivity.start(requireContext(), photoUrl) }
        rvAttachments.adapter = adapter
    }

    companion object {

        fun newInstance(): PhotosFragment {
            return PhotosFragment()
        }
    }
}
