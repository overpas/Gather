package by.overpass.gather.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.util.Consumer
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.snackbar
import by.overpass.gather.ui.auth.login.LoginActivity
import by.overpass.gather.ui.auth.register.add.AddDataStatus
import by.overpass.gather.ui.base.BackPressFragment
import by.overpass.gather.ui.base.personal.DataFragment
import com.bumptech.glide.Glide
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
        tvSignOut.setOnClickListener {
            viewModel.signOut(Runnable {
                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            })
        }
        ivPhotoPreview.setOnClickListener {
            super.onChooseImageClick()
        }
        fabEdit.setOnClickListener {
            viewModel.onProfileModeChanged(Consumer {
                this.handleEditModeChange(it)
            })
        }
    }

    override fun onBind() {
        super.onBind()
        toolbarProfile.setNavigationOnClickListener { navIcon -> requireActivity().finish() }
        viewModel.getUserData(
                Consumer { this.onUserDataLoaded(it) },
                Runnable { this.onUserNotFound() }
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
        ivPhoto.snackbar("Couldn't load user data")
    }

    companion object {

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
