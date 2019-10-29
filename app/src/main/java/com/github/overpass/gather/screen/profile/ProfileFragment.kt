package com.github.overpass.gather.screen.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.UIUtil.snackbar
import com.github.overpass.gather.screen.auth.login.LoginActivity
import com.github.overpass.gather.screen.auth.register.add.AddDataStatus
import com.github.overpass.gather.screen.base.BackPressFragment
import com.github.overpass.gather.screen.base.personal.DataFragment
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.content_personal_data.*
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : DataFragment<ProfileViewModel>(), BackPressFragment {

    override fun createViewModel(): ProfileViewModel {
        return viewModelProvider.get(ProfileViewModel::class.java)
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_profile
    }

    override fun inject() {
        componentManager.getProfileDetailComponent()
                .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeUIMode(false)
    }

    override fun onBind() {
        super.onBind()
        toolbarProfile.setNavigationOnClickListener { navIcon -> requireActivity().finish() }
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

    @OnClick(R.id.tvSignOut)
    fun onSignOutClick() {
        viewModel.signOut {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    @OnClick(R.id.ivPhotoPreview)
    public override fun onChooseImageClick() {
        super.onChooseImageClick()
    }

    @OnClick(R.id.fabEdit)
    internal fun onProfileModeClick() {
        viewModel.onProfileModeChanged {
            this.handleEditModeChange(it)
        }
    }

    override fun handleSuccess(success: AddDataStatus.Success) {
        super.handleSuccess(success)
        requireActivity().recreate()
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
        lavTick?.visibility = if (isEditMode) View.VISIBLE else View.GONE
        tvAvatarPrompt.visibility = if (isEditMode) View.VISIBLE else View.GONE
        tietUsername?.error = null
    }

    private fun onUserDataLoaded(firebaseUser: FirebaseUser) {
        toolbarProfile.title = firebaseUser.displayName
        Glide.with(requireContext())
                .load(firebaseUser.photoUrl)
                .into(ivPhoto)
    }

    private fun onUserNotFound() {
        snackbar(ivPhoto, "Couldn't load user data")
    }

    companion object {

        private const val TAG = "ProfileFragment"

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
