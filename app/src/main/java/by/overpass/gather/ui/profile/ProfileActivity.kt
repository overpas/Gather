package by.overpass.gather.ui.profile

import android.os.Bundle
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.fragment.addToBackStack
import by.overpass.gather.commons.android.fragment.transaction
import by.overpass.gather.ui.base.BackPressActivity
import by.overpass.gather.ui.dialog.PickImageDialogFragment

class ProfileActivity : BackPressActivity<GeneralProfileViewModel>(), PickImageDialogFragment.OnClickListener {

    override fun getLayoutRes(): Int {
        return R.layout.activity_profile
    }

    override fun createViewModel(): GeneralProfileViewModel {
        return viewModelProvider.get(GeneralProfileViewModel::class.java)
    }

    override fun inject() {
        componentManager.getProfileComponent(lifecycle)
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            transaction()
                    .replace(R.id.flProfileContainer, ProfileFragment.newInstance())
                    .addToBackStack(false)
                    .commit()
        }
    }

    override fun onGallery() {
        viewModel.onGallery()
    }

    override fun onCamera() {
        viewModel.onCamera()
    }
}
