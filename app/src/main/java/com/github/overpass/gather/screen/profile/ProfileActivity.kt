package com.github.overpass.gather.screen.profile

import android.os.Bundle
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.FragmentUtils
import com.github.overpass.gather.screen.base.BackPressActivity
import com.github.overpass.gather.screen.dialog.PickImageDialogFragment

class ProfileActivity : BackPressActivity<GeneralProfileViewModel>(), PickImageDialogFragment.OnClickListener {

    override fun getLayoutRes(): Int {
        return R.layout.activity_profile
    }

    override fun createViewModel(): GeneralProfileViewModel {
        return viewModelProvider.get(GeneralProfileViewModel::class.java)
    }

    override fun inject() {
        componentManager.getProfileComponent()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            FragmentUtils.replace(supportFragmentManager, R.id.flProfileContainer,
                    ProfileFragment.newInstance(), false)
        }
    }

    override fun clearComponent() {
        super.clearComponent()
        componentManager.clearProfileComponent()
    }

    override fun onGallery() {
        viewModel.onGallery()
    }

    override fun onCamera() {
        viewModel.onCamera()
    }
}
