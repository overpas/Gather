package com.github.overpass.gather.screen.profile

import android.content.Intent
import android.drm.DrmInfoStatus
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import coil.Coil
import coil.ImageLoaderBuilder
import coil.api.get
import coil.api.load
import coil.decode.DataSource
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.UIUtil.snackbar
import com.github.overpass.gather.model.commons.scope
import com.github.overpass.gather.screen.auth.login.LoginActivity
import com.github.overpass.gather.screen.auth.register.add.AddDataStatus
import com.github.overpass.gather.screen.base.BackPressFragment
import com.github.overpass.gather.screen.base.personal.DataFragment
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.content_personal_data.*
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*

class ProfileFragment : DataFragment<ProfileViewModel>(), BackPressFragment,
        CoroutineScope by scope(Dispatchers.Main) {

    override val layoutRes: Int = R.layout.fragment_profile

    override fun createViewModel(): ProfileViewModel {
        val imageSourceUseCase = ViewModelProviders.of(activity!!)
                .get(GeneralProfileViewModel::class.java)
                .imageSourceUseCase
        val profileViewModel = ViewModelProviders.of(this)
                .get(ProfileViewModel::class.java)
        profileViewModel.setImageSourceUseCase(imageSourceUseCase)
        return profileViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeUIMode(false)
        tvSignOut.setOnClickListener {
            viewModel.signOut {
                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
        ivPhotoPreview.setOnClickListener {
            onChooseImageClick()
        }
        fabEdit.setOnClickListener {
            viewModel.onProfileModeChanged {
                this.handleEditModeChange(it)
            }
        }
    }

    override fun onBind() {
        super.onBind()
        toolbarProfile.setNavigationOnClickListener { navIcon -> activity!!.finish() }
        viewModel.getUserData(
                { this.onUserDataLoaded(it) },
                { this.onUserNotFound() }
        )
    }

    override fun handleBackPress(): Boolean {
        if (viewModel.checkIfIsEditMode()) {
            changeUIMode(false)
            return true
        }
        return false
    }

    override fun handleSuccess(success: AddDataStatus.Success) {
        super.handleSuccess(success)
        activity!!.recreate()
    }

    private fun handleEditModeChange(isEditMode: Boolean) {
        changeUIMode(isEditMode)
        if (!isEditMode) {
            super.onSubmitClick()
        }
    }

    private fun changeUIMode(isEditMode: Boolean) {
        fabEdit.setImageResource(if (isEditMode) R.drawable.ic_tick else R.drawable.ic_pencil)
        tvSignOut.visibility = if (isEditMode) View.GONE else View.VISIBLE
        ivPhotoPreview.visibility = if (isEditMode) View.VISIBLE else View.GONE
        tilUsername.visibility = if (isEditMode) View.VISIBLE else View.GONE
        lavTick!!.visibility = if (isEditMode) View.VISIBLE else View.GONE
        tvAvatarPrompt.visibility = if (isEditMode) View.VISIBLE else View.GONE
        tietUsername!!.error = null
    }

    private fun onUserDataLoaded(firebaseUser: FirebaseUser) {
        toolbarProfile.title = firebaseUser.displayName
        launch {
            val photo = Coil.get(firebaseUser.photoUrl.toString())
            ivPhoto.setImageDrawable(photo)
        }
    }

    private fun onUserNotFound() {
        snackbar(ivPhoto, "Couldn't load user data")
    }

    companion object {

        @JvmStatic
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
